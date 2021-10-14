package io.odero.UserMan.registration;

import io.odero.UserMan.appuser.AppUser;
import io.odero.UserMan.appuser.AppUserRole;
import io.odero.UserMan.appuser.AppUserService;
import io.odero.UserMan.registration.token.ConfirmationToken;
import io.odero.UserMan.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {
       boolean isValidEmail = emailValidator.test(request.getEmail());
       if (!isValidEmail){
           throw new IllegalStateException("Email not valid");
       }
        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
    }

    public String confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Token not found"));
        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("Email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("Token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail();
                return "confirmed";
        )
    }
}
