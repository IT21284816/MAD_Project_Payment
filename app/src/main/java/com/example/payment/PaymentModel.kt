package com.example.payment

data class PaymentModel(
    var cardId: String? = null,
    var cardNum: String? = null,
    var expireyDate: String? = null,
    var cardCVV: String? = null,
    var cardName: String? = null
)