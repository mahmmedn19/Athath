package com.project.athath.ui.ai_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.model.AiRecommendationResponse;
import com.project.athath.data.model.NextRecommendationResponse;
import com.project.athath.data.model.ResponseModel;
import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AiViewModel extends ViewModel {

    private final AthathRepository aiRepository;
    private final MutableLiveData<Map<String, String>> userAnswers = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Result<AiRecommendationResponse>> recommendationResult = new MutableLiveData<>();
    private final MutableLiveData<Result<NextRecommendationResponse>> nextRecommendationResult = new MutableLiveData<>();
    private final MutableLiveData<Result<List<ResponseModel.DetectedObject>>> uploadResult = new MutableLiveData<>();

    @Inject
    public AiViewModel(AthathRepository aiRepository) {
        this.aiRepository = aiRepository;
    }

    public LiveData<Map<String, String>> getUserAnswers() {
        return userAnswers;
    }

    public LiveData<Result<AiRecommendationResponse>> getRecommendationResult() {
        return recommendationResult;
    }

    public LiveData<Result<NextRecommendationResponse>> getNextRecommendationResult() {
        return nextRecommendationResult;
    }

    public void updateAnswer(String question, String answer) {
        Map<String, String> currentAnswers = userAnswers.getValue();
        if (currentAnswers != null) {
            currentAnswers.put(question, answer);
            userAnswers.setValue(currentAnswers);
        }
    }

    public void submitAnswers() {
        if (userAnswers.getValue() == null || userAnswers.getValue().size() != AiFragment.questions.size()) {
            recommendationResult.postValue(Result.error("Please answer all questions."));
            return;
        }

        recommendationResult.postValue(Result.loading());
        // Convert userAnswers to required JSON format
        Map<String, String> formattedRequest = new LinkedHashMap<>();
        formattedRequest.put("Primary_Purpose", userAnswers.getValue().get("What is the primary purpose of the room?"));
        formattedRequest.put("Design_Style", userAnswers.getValue().get("What is your preferred design style?"));
        formattedRequest.put("Furniture_Type", userAnswers.getValue().get("Do you prefer ready-made or multifunctional furniture?"));
        formattedRequest.put("Room_Size", userAnswers.getValue().get("What is the size of the room in square meters?"));
        formattedRequest.put("Dimension_Constraints", userAnswers.getValue().get("Do you have any constraints on furniture dimensions?"));
        formattedRequest.put("Budget", userAnswers.getValue().get("What is your budget for furniture?"));
        formattedRequest.put("Storage_Needs", userAnswers.getValue().get("Do you need additional storage solutions?"));

        aiRepository.getRecommendations(formattedRequest).observeForever(recommendationResult::setValue);
    }

    public void fetchNextRecommendation() {
        nextRecommendationResult.postValue(Result.loading());

        aiRepository.getNextRecommendation().observeForever(nextRecommendationResult::postValue);
    }


    public void uploadImage(File file) {
        aiRepository.uploadImage(file).observeForever(uploadResult::setValue);
    }

    public LiveData<Result<List<ResponseModel.DetectedObject>>> getUploadResult() {
        return uploadResult;
    }
}
