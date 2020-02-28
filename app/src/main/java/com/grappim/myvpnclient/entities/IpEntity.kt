package com.grappim.myvpnclient.entities

import com.google.gson.annotations.SerializedName

data class IpEntity(

  @SerializedName("as")
  val asX: As?,

  @SerializedName("domains")
  val domains: List<String>?,

  @SerializedName("ip")
  val ip: String?,

  @SerializedName("isp")
  val isp: String?,

  @SerializedName("location")
  val location: Location?

)