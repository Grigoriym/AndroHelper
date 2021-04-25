package com.grappim.androHelper.data.remote.model.ip

import com.google.gson.annotations.SerializedName

data class IpEntityDTO(

  @SerializedName("as")
  val asX: AsDTO?,

  @SerializedName("domains")
  val domains: List<String>?,

  @SerializedName("ip")
  val ip: String?,

  @SerializedName("isp")
  val isp: String?,

  @SerializedName("location")
  val location: LocationDTO?

)

data class LocationDTO(

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

data class AsDTO(
  @SerializedName("asn")
  val asn: Int?,

  @SerializedName("domain")
  val domain: String?,

  @SerializedName("name")
  val name: String?,

  @SerializedName("route")
  val route: String?,

  @SerializedName("type")
  val type: String?

)