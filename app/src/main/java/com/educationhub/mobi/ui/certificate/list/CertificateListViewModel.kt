package com.educationhub.mobi.ui.certificate.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.educationhub.mobi.repository.certificate.CertificateRepository
import com.educationhub.mobi.repository.certificate.CertificateResponse
import kotlinx.coroutines.Dispatchers

class CertificateListViewModel(private val repository: CertificateRepository = CertificateRepository()) :
    ViewModel() {
    lateinit var certResponseLiveData: LiveData<CertificateResponse?>

    init {
        getCertificateResponse()
    }

    fun getCertificateResponse() {
        certResponseLiveData = liveData(Dispatchers.IO) {
            emit(repository.getCompletedCourse())
        }
    }

}