package com.runmail.runmail.emailDTO;

import java.util.Date;

public record EmailResponseDTO (

        String subject,

        String sender,

        String body,

        Date date
) {}
