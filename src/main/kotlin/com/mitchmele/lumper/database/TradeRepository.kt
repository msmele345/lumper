package com.mitchmele.lumper.database

import com.mitchmele.lumper.trade.Trade
import org.springframework.data.jpa.repository.JpaRepository

interface TradeRepository : JpaRepository<Trade, Int>