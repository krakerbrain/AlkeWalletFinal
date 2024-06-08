package com.example.alkewalletfinal.model.network

data class PaginatedTransactionResponse(val previousPage: String?,
                                        val nextPage: String?,
                                        val data: List<TransactionResponse>)
