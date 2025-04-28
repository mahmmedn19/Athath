package com.project.athath.ui.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z ]+$";
    //phone start with +966 and 9 digits
    private static final String PHONE_REGEX = "^\\+966[0-9]{9}$";
    private static final Map<EditText, TextWatcher> textWatcherMap = new HashMap<>();

    public static boolean validateDataWithConstraint(TextInputLayout textInputLayout, String text) {
        if (isEmpty(text)) {
            setError(textInputLayout, "This field cannot be empty");
            return false;
        } else if (text.length() < 4) {
            setError(textInputLayout, "At least 4 char");
            return false;
        }
        clearError(textInputLayout);
        return true;
    }

    public static boolean validateData(TextInputLayout textInputLayout, String text) {
        if (isEmpty(text)) {
            setError(textInputLayout, "This field cannot be empty");
            return false;
        }
        clearError(textInputLayout);
        return true;
    }


    public static boolean validateEmail(TextInputLayout emailTextInputLayout, String email) {
        if (isEmpty(email)) {
            setError(emailTextInputLayout, "Email field cannot be empty");
            return false;
        } else if (!isValidEmailFormat(email)) {
            if (emailTextInputLayout != null) {
                setError(emailTextInputLayout, "Invalid email address");
            }
            return false;
        }
        clearError(emailTextInputLayout);
        return true;
    }

    public static boolean validatePassword(TextInputLayout passwordTextInputLayout, String password) {
        if (isEmpty(password)) {
            setError(passwordTextInputLayout, "Password field cannot be empty");
            return false;

        } else if (!isValidPasswordFormat(password)) {
            setError(passwordTextInputLayout, "Password must contain at least 1 uppercase letter, 1 number, and 1 special character");
            return false;
        } else if (password.length() < 8) {
            setError(passwordTextInputLayout, "Password must be at least 8 characters long");
            return false;
        } else if (password.length() > 12) {
            setError(passwordTextInputLayout, "Password must not exceed 12 characters");
            return false;
        }
        clearError(passwordTextInputLayout);
        return true;
    }

    public static boolean validateConfirmPassword(TextInputLayout confirmPasswordTextInputLayout, String password, String confirmPassword) {
        if (isEmpty(confirmPassword)) {
            setError(confirmPasswordTextInputLayout, "Confirm password field cannot be empty");
            return false;
        } else if (!password.equals(confirmPassword)) {
            setError(confirmPasswordTextInputLayout, "Passwords do not match");
            return false;
        }
        clearError(confirmPasswordTextInputLayout);
        return true;
    }

    public static boolean validateUsername(TextInputLayout nameTextInputLayout, String name) {
        if (isEmpty(name)) {
            setError(nameTextInputLayout, "Full Name field cannot be empty");
            return false;
        } else if (!isValidUsernameFormat(name)) {
            setError(nameTextInputLayout, "Full Name can only contain letters");
            return false;
        } else if (name.length() < 3) {
            setError(nameTextInputLayout, "Full Name must be at least 3 characters long");
            return false;
        } else if (name.length() > 25) {
            setError(nameTextInputLayout, "Full Name must not exceed 25 characters");
            return false;
        }
        clearError(nameTextInputLayout);
        return true;
    }

    public static boolean validateStoreName(TextInputLayout storeNameTextInputLayout, String storeName) {
        if (isEmpty(storeName)) {
            setError(storeNameTextInputLayout, "Store name cannot be empty");
            return false;
        }
        else if (storeName.length() < 3) {
            setError(storeNameTextInputLayout, "Store name must be at least 3 characters long");
            return false;
        } else if (storeName.length() > 25) {
            setError(storeNameTextInputLayout, "Store name must not exceed 25 characters");
            return false;
        }
        clearError(storeNameTextInputLayout);
        return true;
    }


    public static boolean validatePhone(TextInputLayout phoneTextInputLayout, String phone) {
        if (isEmpty(phone)) {
            setError(phoneTextInputLayout, "Phone field cannot be empty");
            return false;
        } else if (!Pattern.compile(PHONE_REGEX).matcher(phone).matches()) {
            setError(phoneTextInputLayout, "Phone number must start with +966 and contain 9 digits");
            return false;
        }
        clearError(phoneTextInputLayout);
        return true;
    }

    private static boolean isEmpty(String input) {
        return TextUtils.isEmpty(input);
    }

    public static boolean isValidEmailFormat(String email) {
        return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    private static boolean isValidPasswordFormat(String password) {
        return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
    }

    private static boolean isValidUsernameFormat(String name) {
        return Pattern.compile(USERNAME_REGEX).matcher(name.trim()).matches();
    }

    private static void setError(TextInputLayout textInputLayout, String errorMessage) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(errorMessage);
    }

    private static void clearError(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public static void clearErrorOnTextChange(TextInputLayout textInputLayout) {
        if (textInputLayout == null || textInputLayout.getEditText() == null) {
            return; // Prevent NullPointerException
        }

        EditText editText = textInputLayout.getEditText();

        // Remove any existing TextWatcher to prevent duplicate listeners
        removeExistingTextWatcher(editText);

        // Create a new TextWatcher
        TextWatcher textWatcher = new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString(); // Store previous text value before modification
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clearError(textInputLayout); // Clear error dynamically when typing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ensure the field is not left empty
                if (editable.toString().trim().isEmpty()) {
                    setError(textInputLayout, "This field cannot be empty");
                }
            }
        };

        // Add the TextWatcher to the EditText
        editText.addTextChangedListener(textWatcher);

        // Store the TextWatcher reference
        textWatcherMap.put(editText, textWatcher);
    }


    private static void removeExistingTextWatcher(EditText editText) {
        // Check if a TextWatcher is already registered for this EditText
        if (textWatcherMap.containsKey(editText)) {
            editText.removeTextChangedListener(textWatcherMap.get(editText));
            textWatcherMap.remove(editText); // Remove reference after unbinding
        }
    }
    public static void setupFieldHelperText(TextInputLayout inputLayout, String helperText) {
        inputLayout.setHelperText(helperText);
        inputLayout.setHelperTextEnabled(true);
    }
}