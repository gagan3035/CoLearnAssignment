package com.example.colearn.models

/**
 * Created by Gagan on 01/03/21.
 */
data class FilterItem(
    val name: String,
    val filterVal: String?,
    var isChecked: Boolean = false) {
}