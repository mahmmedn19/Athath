package com.project.athath.ui.ai_screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AiViewModel extends ViewModel {

    private final AthathRepository aiRepository;
    private final MutableLiveData<Map<String, String>> userAnswers = new MutableLiveData<>(new HashMap<>());
    private final MutableLiveData<Result<Boolean>> submissionResult = new MutableLiveData<>();

    @Inject
    public AiViewModel(AthathRepository aiRepository) {
        this.aiRepository = aiRepository;
    }

    public LiveData<Map<String, String>> getUserAnswers() {
        return userAnswers;
    }

    public LiveData<Result<Boolean>> getSubmissionResult() {
        return submissionResult;
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
            submissionResult.postValue(Result.error("Please answer all questions."));
            return;
        }

        submissionResult.postValue(Result.loading());

        // aiRepository.sendAiRequest(userAnswers.getValue(), result -> submissionResult.postValue(result));
    }
}
