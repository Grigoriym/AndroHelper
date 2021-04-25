package com.grappim.androHelper.entities

import com.google.gson.annotations.SerializedName

data class Location(

  @SerializedName("city")
  val city: String?,

  @SerializedName("country")
  val country: String?,

  @SerializedName("geonameId")
  val geonameId: Int?,

  @SerializedName("lat")
  val lat: Double?,

  @SerializedName("lng")
  val lng: Double?,

  @SerializedName("postalCode")
  val postalCode: String?,

  @SerializedName("region")
  val region: String?,

  @SerializedName("timezone")
  val timezone: String?

)