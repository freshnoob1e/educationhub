package com.educationhub.mobi.repository.user

import com.educationhub.mobi.model.UserInfo

data class UserInfoResponse(
    var UserInfo: UserInfo? = null,
    var exception: Exception? = null
)