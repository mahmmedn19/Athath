package com.project.athath.ui.ai_screen;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.project.athath.R;
import com.project.athath.data.utils.Result;
import com.project.athath.databinding.FragmentAiBinding;
import com.project.athath.databinding.ItemQuestionBinding;
import com.project.athath.ui.base.BaseFragment;
import com.project.athath.ui.utils.DialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AiFragment extends BaseFragment<FragmentAiBinding> {

    private LinearLayout questionContainer;
    private AiViewModel viewModel;
    Dialog dialog = null;
    public static final List<Question> questions = Arrays.asList(
            new Question("What is the primary purpose of the room?", Arrays.asList("Work", "Relaxation", "Play", "Dining")),
            new Question("What is your preferred design style?", Arrays.asList("Modern", "Traditional", "Rustic", "Industrial", "Scandinavian")),
            new Question("What colors do you prefer for furniture?", Arrays.asList("White", "Beige", "Gray", "Brown", "Black", "Bright Colors")),
            new Question("What materials do you prefer for furniture?", Arrays.asList("Wood", "Metal", "Glass", "Fabric")),
            new Question("Do you prefer ready-made or multifunctional furniture?", Arrays.asList("Ready-made", "Multifunctional")),
            new Question("What is the size of the room in square meters?", Arrays.asList("Less than 10 sqm", "Between 10 and 20 sqm", "More than 20 sqm")),
            new Question("Do you have any constraints on furniture dimensions?", Arrays.asList("Yes", "No")),
            new Question("What is your budget for furniture?", Arrays.asList("Less than 1000 SAR", "Between 1000 and 5000 SAR", "More than 5000 SAR")),
            new Question("Do you prefer eco-friendly furniture?", Arrays.asList("Yes", "No")),
            new Question("Do you need additional storage solutions?", Arrays.asList("Yes", "No")),
            new Question("Do you require furniture that is easy to assemble and move?", Arrays.asList("Yes", "No")),
            new Question("Are there children or pets in the house?", Arrays.asList("Yes", "No")),
            new Question("Do you prefer comfortable or practical furniture?", Arrays.asList("Comfortable", "Practical")),
            new Question("Do you have any other specific requests?", Arrays.asList("Stain-resistant", "Easy to clean", "Durable for heavy use"))
    );

    @Override
    protected String getTAG() {
        return "AiFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_ai;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(AiViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("AI Suggestions");
        showBackButton(false);

        questionContainer = binding.questionContainer;
        addQuestions();

        binding.btnShowCatelog.setOnClickListener(view -> {
            if (validateAllQuestionsAnswered()) {
                viewModel.submitAnswers();
            } else {
                Toast.makeText(requireContext(), "Please answer all questions before submitting!", Toast.LENGTH_SHORT).show();
            }
        });

        observeViewModel();
    }

    private void addQuestions() {
        LayoutInflater inflater = getLayoutInflater();
        for (Question question : questions) {
            ItemQuestionBinding itemBinding = DataBindingUtil.inflate(inflater, R.layout.item_question, questionContainer, false);
            itemBinding.setQuestionText(question.getQuestionText());

            ChipGroup chipGroup = itemBinding.getRoot().findViewById(R.id.chipGroup);
            for (String option : question.getOptions()) {
                Chip chip = new Chip(requireContext());
                chip.setText(option);
                chip.setCheckable(true);
                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) viewModel.updateAnswer(question.getQuestionText(), option);
                });
                chipGroup.addView(chip);
            }
            questionContainer.addView(itemBinding.getRoot());
        }
    }

    private boolean validateAllQuestionsAnswered() {
        return Objects.requireNonNull(viewModel.getUserAnswers().getValue()).size() == questions.size();
    }

    private void observeViewModel() {
        viewModel.getRecommendationResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                DialogUtils.showLoadingDialog(requireContext(), "Fetching recommendation...");
            } else if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                DialogUtils.hideLoadingDialog();
                showRecommendationDialog(result.getData().getImageBase64());
            } else if (result.getStatus() == Result.Status.ERROR) {
                DialogUtils.hideLoadingDialog();
                Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRecommendationDialog(String base64Image) {
        if (base64Image == null || base64Image.isEmpty()) return;

        Bitmap bitmap = decodeBase64ToBitmap(base64Image);
        if (bitmap == null) return;

        // Dismiss any existing dialog before showing a new one
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_recommendation);
        dialog.setCancelable(false);

        ImageView imgRecommendation = dialog.findViewById(R.id.imgRecommendation);
        imgRecommendation.setImageBitmap(bitmap);
        dialog.findViewById(R.id.btnClose).setOnClickListener(v -> dialog.dismiss());

        dialog.findViewById(R.id.btnViewDetails).setOnClickListener(v -> {
            dialog.dismiss();
            onShowProductsClicked(base64Image);
        });

        dialog.findViewById(R.id.btnNextRecommendation).setOnClickListener(v -> {
            dialog.dismiss();

            // 🛑 Remove previous observers to avoid duplicate triggers
            viewModel.getNextRecommendationResult().removeObservers(getViewLifecycleOwner());

            // ✅ Observe next recommendation response correctly
            viewModel.fetchNextRecommendation();
            viewModel.getNextRecommendationResult().observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.LOADING) {
                    DialogUtils.showLoadingDialog(requireContext(), "Fetching recommendation...");
                } else if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                    DialogUtils.hideLoadingDialog();
                    showRecommendationDialog(result.getData().getImageBase64()); // Show new recommendation
                } else if (result.getStatus() == Result.Status.ERROR) {
                    DialogUtils.hideLoadingDialog();
                    Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }


    private void onShowProductsClicked(String imageBase64) {
        Bitmap bitmap = decodeBase64ToBitmap(imageBase64);
        if (bitmap == null) return;

        File imageFile = bitmapToFile(bitmap);
        if (imageFile == null) return;

        viewModel.uploadImage(imageFile);
        viewModel.getUploadResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                DialogUtils.showLoadingDialog(requireContext(), "Uploading image...");
            } else if (result.getStatus() == Result.Status.SUCCESS && result.getData() != null) {
                DialogUtils.hideLoadingDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("detectedObjects", new ArrayList<>(result.getData()));
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_aiFragment_to_catalogDetailsFragment, bundle);
            } else if (result.getStatus() == Result.Status.ERROR) {
                DialogUtils.hideLoadingDialog();
                Toast.makeText(requireContext(), "Error: " + result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        try {
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            return null;
        }
    }

    private File bitmapToFile(Bitmap bitmap) {
        try {
            File file = new File(requireContext().getCacheDir(), "catalog_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    // Helper class to hold question data
    static class Question {
        private final String questionText;
        private final List<String> options;

        public Question(String questionText, List<String> options) {
            this.questionText = questionText;
            this.options = options;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<String> getOptions() {
            return options;
        }
    }
}
