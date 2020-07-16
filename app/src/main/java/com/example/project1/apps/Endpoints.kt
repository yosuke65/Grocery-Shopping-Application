package com.example.project1.apps

class Endpoints {
    companion object {
        private const val URL_CATEGORY = "category"
        private const val URL_SUB_CATEGORY = "subcategory"
        private const val URL_PRODUCTS = "products"
        private const val URL_REGISTER = "auth/register"
        private const val URL_LOGIN = "auth/login"


        fun getCategory() = Config.BASE_URL + URL_CATEGORY

        fun getSubCategory(catId: Int) = "${Config.BASE_URL + URL_SUB_CATEGORY}/$catId"
        fun getImage(image: String) = Config.IMAGE_URL + image

        fun getProductsBySubId(subId: Int) = "${Config.BASE_URL + URL_PRODUCTS}/sub/$subId"

        fun postRegister() = "${Config.BASE_URL + URL_REGISTER}"
        fun postLogin() = "${Config.BASE_URL + URL_LOGIN}"

    }
}