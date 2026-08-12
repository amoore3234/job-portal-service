package com.portal.job_portal_service.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private boolean confirmFirstTimeLogin;
}
