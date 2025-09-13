package com.example.vidyaksha.presentation.session

import com.example.vidyaksha.domain.model.Session
import com.example.vidyaksha.domain.model.Subject

data class SessionState(
    val subjects: List<Subject> = emptyList(),
    val sessions: List<Session> = emptyList(),
    val relatedToSubject: String? = null,
    val subjectId: Int? = null,
    val session: Session? = null
)
