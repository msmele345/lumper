package com.mitchmele.lumper.trade

import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "TRADE")
data class Trade(

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "TRADE_ID", nullable = false, unique = true)
        private var id: Int? = null,

        @javax.persistence.Column(name = "BID_ID")
        private val bidId: Int? = null,

        @Column(name = "ASK_ID")
        private val askId: Int? = null,

        @Column(name = "SYMBOL")
        private val symbol: String? = null,

        @Column(name = "Tradeprice")
        private val tradePrice: BigDecimal? = null,

        @Column(name = "CREATED_TS", updatable = false)
        @CreationTimestamp
        private val timeStamp: Date? = null
)