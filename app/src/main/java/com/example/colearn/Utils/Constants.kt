package com.example.colearn.Utils

/**
 * Created by Gagan on 01/03/21.
 */
object Constants {

    const val SORT_BY_FILTER = "sortby"
    const val COLOR_FILTER = "color"
    const val ORIENTATION_FILTER = "orientation"


    const val ORDER_BY_LATEST = "latest"

    interface OrderByFilter {

        companion object {
            const val ORDER_BY_LATEST: String = "latest"
            const val ORDER_BY_RELEVANT: String = "relevant"
        }

    }

    interface OrderByColor {

        companion object {
            const val ORDER_BY_BNW: String = "black_and_white"
            const val ORDER_BY_Any: String = "any"
        }

    }

    interface OrderByOrientation {

        companion object {
            const val ORDER_BY_LANDSCAPE: String = "landscape"
            const val ORDER_BY_PORTRAIT: String = "portrait"
            const val ORDER_BY_SQUARISH: String = "squarish"
        }

    }
}