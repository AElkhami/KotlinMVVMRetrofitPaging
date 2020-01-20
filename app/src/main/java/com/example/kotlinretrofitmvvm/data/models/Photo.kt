package com.example.kotlinretrofitmvvm.data.models

data class Photo(
	val owner: String? = null,
	val server: String? = null,
	val ispublic: Int? = null,
	val isfriend: Int? = null,
	val farm: Int? = null,
	val id: String? = null,
	val secret: String? = null,
	val title: String? = null,
	val isfamily: Int? = null){

	fun getPhotoUrl(): String{
		return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg"
	}
}
