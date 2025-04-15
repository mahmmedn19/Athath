package com.project.athath.ui.admin_screen.add_ai_link;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.athath.data.repository.app_repo.AthathRepository;
import com.project.athath.data.utils.Result;

import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AddLinkViewModel extends ViewModel {
    public MutableLiveData<String> url = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final AthathRepository appRepository;

    @Inject
    public AddLinkViewModel(AthathRepository appRepository) {
        this.appRepository = appRepository;
    }


    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$"
    );

    public boolean validateUrl() {
        String urlInput = url.getValue();
        if (urlInput == null || urlInput.isEmpty() || !URL_PATTERN.matcher(urlInput).matches()) {
            errorMessage.setValue("Invalid URL format");
            return false;
        } else {
            errorMessage.setValue(null);
            return true;
        }
    }

    public LiveData<Result<String>> addAILink() {
        return appRepository.addAILink(url.getValue());
    }
}