package com.example.kotlinretrofitmvvm.data.models

import androidx.paging.PagedList

data class Photos(
	val perpage: Int? = null,
	val total: Int? = null,
	val pages: Int? = null,
	val photo: MutableList<Photo>? = null,
	val page: Int? = null
)
