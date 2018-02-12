package com.ruchij.models

import org.joda.time.DateTime

case class Ping(id: String, timestamp: DateTime, ip: String)
