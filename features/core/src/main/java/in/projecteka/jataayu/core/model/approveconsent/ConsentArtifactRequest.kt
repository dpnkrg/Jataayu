package `in`.projecteka.jataayu.core.model.approveconsent

import com.google.gson.annotations.SerializedName

data class ConsentArtifactRequest (

	@SerializedName("consents") val consents : List<ConsentArtifact>
)