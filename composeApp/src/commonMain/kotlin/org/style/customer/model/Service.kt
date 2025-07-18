package org.style.customer.model

data class Service(
    val id: String,
    val name: String,
    val duration: String,
    val price: String,
    val description: String,
    val femaleOnly: Boolean,
    val category: String
)