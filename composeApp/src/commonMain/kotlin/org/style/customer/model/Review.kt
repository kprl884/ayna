package org.style.customer.model

data class Review(
    val id: String,
    val customerName: String,
    val initials: String,
    val date: String,
    val rating: Float,
    val comment: String
) 