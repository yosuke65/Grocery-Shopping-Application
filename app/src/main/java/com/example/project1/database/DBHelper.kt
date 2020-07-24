package com.example.project1.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf
import com.example.project1.models.Address
import com.example.project1.models.Product

class DBHelper(mContext: Context) : SQLiteOpenHelper(mContext, DB_NAME, null, DB_VERSION) {

    val db = writableDatabase

    companion object {
        const val DB_NAME = "sc_database"
        const val DB_VERSION = 4


        //Table for cart
        const val TABLE_NAME_CART = "cart"
        const val COL_PID = "pid"
        const val COL_NAME = "name"
        const val COL_PRICE = "price"
        const val COL_MRP = "mrp"
        const val COL_IMAGE = "image"
        const val COL_QUANTITY = "quantity"
        const val CREATE_CART_TABLE =
            "create table $TABLE_NAME_CART($COL_PID CHAR(50), $COL_NAME CHAR(50), $COL_PRICE REAL, $COL_MRP REAL,$COL_IMAGE CHAR(50),$COL_QUANTITY INTCOL_O)"
        const val DROP_CART_TABLE = "drop table if exists $TABLE_NAME_CART"


        //Table for Address
//        const val TABLE_NAME_ADDRESS = "address"
//        const val COL_UID = "uid"
//        const val COL_HOUSE_NO = "houseNo"
//        const val COL_STREET_NAME = "streetName"
//        const val COL_TYPE = "type"
//        const val COL_CITY = "city"
//        const val COL_PINCODE = "pincode"
//        const val COL_BILLING_NAME = "billingName"
//        const val CREATE_ADDRESS_TABLE =
//            "create table $TABLE_NAME_ADDRESS($COL_UID CHAR(50), $COL_HOUSE_NO CHAR(50), $COL_STREET_NAME CHAR(50), $COL_TYPE CHAR(50), $COL_CITY CHAR(50), $COL_PINCODE CHAR(50), $COL_BILLING_NAME CHAR(50))"
//        const val DROP_ADDRESS_TABLE = "drop table if exists $TABLE_NAME_ADDRESS"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(CREATE_CART_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        db?.execSQL(DROP_CART_TABLE)
        onCreate(db)
    }

    fun addToCart(product: Product, quantity: Int) {
        if (!isProductInCart(product)) {
            var contentValues = ContentValues()
            contentValues.put(COL_PID, product._id)
            contentValues.put(COL_NAME, product.productName)
            contentValues.put(COL_PRICE, product.price)
            contentValues.put(COL_MRP, product.mrp)
            contentValues.put(COL_IMAGE, product.image)
            contentValues.put(COL_QUANTITY, quantity)
            db.insert(TABLE_NAME_CART, null, contentValues)
        } else {
            //Duplicate item
            var qt = quantity
            updateItem(product._id, ++qt)
        }
    }


    fun isProductInCart(product: Product): Boolean {
        var query = "select * from $TABLE_NAME_CART where $COL_PID = ?"
        var cursor = db.rawQuery(query, arrayOf(product._id))
        var count = cursor.count
        return count != 0
    }

    fun isProductInCart(): Boolean {
        var query = "select * from $TABLE_NAME_CART"
        var cursor = db.rawQuery(query, null, null)

        return cursor.count != 0
    }


    fun getItemQuantity(id: String): Int {
        var count: String = "0"
        var columns = arrayOf(
            COL_QUANTITY
        )
        var whereClause = "$COL_PID = ?"
        var whereArgs = arrayOf(id)
        var cursor = db.query(TABLE_NAME_CART, columns, whereClause, whereArgs, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getString(cursor.getColumnIndex(COL_QUANTITY))
        }
        cursor.close()
        return count.toInt()
    }


    fun readCart(): ArrayList<Product> {
        var cartList: ArrayList<Product> = ArrayList()
        var columns = arrayOf(
            COL_PID,
            COL_NAME,
            COL_PRICE,
            COL_MRP,
            COL_IMAGE,
            COL_QUANTITY
        )
        var cursor = db.query(TABLE_NAME_CART, columns, null, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            do {
                var pid = cursor.getString(cursor.getColumnIndex(COL_PID))
                var name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                var price = cursor.getDouble(cursor.getColumnIndex(COL_PRICE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COL_MRP))
                var image = cursor.getString(cursor.getColumnIndex(COL_IMAGE))
                var quantity = cursor.getInt(cursor.getColumnIndex(COL_QUANTITY))

                cartList.add(
                    Product(
                        null, pid, null, null, null,
                        image, mrp, null, price, name, quantity, null, null, null,null, null
                    )
                )

            } while (cursor.moveToNext())
            cursor.close()
        }
        return cartList
    }

    fun updateItem(id: String, quantity: Int) {
        val whereClause = "$COL_PID = ?"
        val whereArgs = arrayOf(id)
        var contentValues = ContentValues()
        contentValues.put(COL_QUANTITY, quantity)
        db.update(TABLE_NAME_CART, contentValues, whereClause, whereArgs)
    }


    fun deleteItem(id: String) {
        val whereClause = "$COL_PID = ?"
        val whereArgs = arrayOf(id)
        db.delete(TABLE_NAME_CART, whereClause, whereArgs)
    }

    fun deleteItem(){
        db.delete(TABLE_NAME_CART,null, null)
    }

//    fun saveAddress(address: Address) {
//        var contentValues = ContentValues()
//        contentValues.put(COL_UID, address.userId)
//        contentValues.put(COL_HOUSE_NO, address.houseNo)
//        contentValues.put(COL_STREET_NAME, address.streetName)
//        contentValues.put(COL_TYPE, address.type)
//        contentValues.put(COL_CITY, address.city)
//        contentValues.put(COL_PINCODE, address.pincode)
//        contentValues.put(COL_BILLING_NAME, address.billingName)
//        db.insert(TABLE_NAME_ADDRESS, null, contentValues)
//    }

//    fun readAddress(userId: String): ArrayList<Address> {
//        var mList: ArrayList<Address> = ArrayList()
//        var columns = arrayOf(
//            COL_UID,
//            COL_HOUSE_NO,
//            COL_STREET_NAME,
//            COL_TYPE,
//            COL_CITY,
//            COL_PINCODE,
//            COL_BILLING_NAME
//        )
//        var cursor = db.query(TABLE_NAME_ADDRESS, columns, null, null, null, null, null)
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                var uid = cursor.getString(cursor.getColumnIndex(COL_UID))
//                var houseId = cursor.getString(cursor.getColumnIndex(COL_HOUSE_NO))
//                var streetName = cursor.getString(cursor.getColumnIndex(COL_STREET_NAME))
//                var type = cursor.getString(cursor.getColumnIndex(COL_TYPE)).toInt()
//                var city = cursor.getString(cursor.getColumnIndex(COL_CITY))
//                var pincode = cursor.getString(cursor.getColumnIndex(COL_PINCODE))
//                var billingName = cursor.getString(cursor.getColumnIndex(COL_BILLING_NAME))
//                var address =
//                    Address(null, uid, houseId, streetName, type, city, pincode, billingName)
//                mList.add(address)
//            } while (cursor.moveToNext())
//            cursor.close()
//        }
//        return mList
//    }
}