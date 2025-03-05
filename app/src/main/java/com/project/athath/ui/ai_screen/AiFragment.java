package com.project.athath.ui.ai_screen;

import android.view.LayoutInflater;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AiFragment extends BaseFragment<FragmentAiBinding> {

    private LinearLayout questionContainer;
    private AiViewModel viewModel;

    public static final List<Question> questions = Arrays.asList(
            new Question("1. What type of room are you looking for?",
                    Arrays.asList("Bedroom", "Living Room", "Office", "Kids' Room", "Dining Room")),

            new Question("2. What is the primary purpose of the room?",
                    Arrays.asList("Work", "Relaxation", "Play", "Dining")),

            new Question("3. What is your preferred design style?",
                    Arrays.asList("Modern", "Traditional", "Rustic", "Industrial", "Scandinavian")),

            new Question("4. What colors do you prefer for furniture?",
                    Arrays.asList("White", "Beige", "Gray", "Brown", "Black", "Bright Colors")),

            new Question("5. What materials do you prefer for furniture?",
                    Arrays.asList("Wood", "Metal", "Glass", "Fabric")),

            new Question("6. Do you prefer ready-made or multifunctional furniture?",
                    Arrays.asList("Ready-made", "Multifunctional")),

            new Question("7. What is the size of the room in square meters?",
                    Arrays.asList("Less than 10 sqm", "Between 10 and 20 sqm", "More than 20 sqm")),

            new Question("8. Do you have any constraints on furniture dimensions?",
                    Arrays.asList("Yes", "No")),

            new Question("9. What is your budget for furniture?",
                    Arrays.asList("Less than 1000 SAR", "Between 1000 and 5000 SAR", "More than 5000 SAR")),

            new Question("10. Do you prefer eco-friendly furniture?",
                    Arrays.asList("Yes", "No")),

            new Question("11. Do you need additional storage solutions?",
                    Arrays.asList("Yes", "No")),

            new Question("12. Do you require furniture that is easy to assemble and move?",
                    Arrays.asList("Yes", "No")),

            new Question("13. Are there children or pets in the house?",
                    Arrays.asList("Yes", "No")),

            new Question("14. Do you prefer comfortable or practical furniture?",
                    Arrays.asList("Comfortable", "Practical")),

            new Question("15. Do you have any other specific requests?",
                    Arrays.asList("Stain-resistant", "Easy to clean", "Durable for heavy use"))
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


        // Handle navigation to Catalog
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
                    if (isChecked) {
                        viewModel.updateAnswer(question.getQuestionText(), option);
                    }
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
        viewModel.getSubmissionResult().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                Toast.makeText(requireContext(), "Submitting...", Toast.LENGTH_SHORT).show();
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Submitted successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_aiFragment_to_catalogFragment);
            } else if (result.getStatus() == Result.Status.ERROR) {
                Toast.makeText(requireContext(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
