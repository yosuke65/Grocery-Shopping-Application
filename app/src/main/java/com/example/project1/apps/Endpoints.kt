package com.example.project1.apps

class Endpoints {
    companion object {
        private const val URL_CATEGORY = "category"
        private const val URL_SUB_CATEGORY = "subcategory"
        private const val URL_PRODUCTS = "products"
        private const val URL_REGISTER = "auth/register"
        private const val URL_LOGIN = "auth/login"
        private const val URL_ADDRESS = "address"
        private const val URL_ORDER = "orders"


        fun getCategoryURL() = Config.BASE_URL + URL_CATEGORY

        fun getSubCategoryURL(catId: Int) = "${Config.BASE_URL + URL_SUB_CATEGORY}/$catId"

        fun getImageURL(image: String) = Config.IMAGE_URL + image

        fun getProductsURL(subId: Int) = "${Config.BASE_URL + URL_PRODUCTS}/sub/$subId"

        fun getRegisterURL() = "${Config.BASE_URL + URL_REGISTER}"

        fun getLoginURL() = "${Config.BASE_URL + URL_LOGIN}"

        fun getAddressURL() = "${Config.BASE_URL + URL_ADDRESS}"

        fun getAddressURL(id: String) = "${Config.BASE_URL + URL_ADDRESS}/$id"

        fun getOrdersURL()= "${Config.BASE_URL + URL_ORDER}"

        fun getOrdersURL(userId:String?) = "${Config.BASE_URL + URL_ORDER}/$userId"


    }
}