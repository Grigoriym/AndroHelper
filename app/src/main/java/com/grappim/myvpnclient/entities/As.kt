package com.grappim.myvpnclient.entities

import com.google.gson.annotations.SerializedName

data class As(
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