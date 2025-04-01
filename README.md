# Netty ByteBuf Util (0.2.0)

This project contains various [extension methods and properties](src/main/kotlin/com/exactpro/th2/netty/bytebuf/util/ByteBufUtil.kt) for Netty's ByteBuf

# Changelog

## Unreleased

### Updated:

* th2 bom now `4.11.0` that comes from th2 gradle plugins `0.2.4`
* junit-jupiter now `5.12.1`
* commons-lang3 now `3.17.0`

## 0.2.0

* add ByteBuf extension functions for reading writing unsigned and signed ints, ASCII char, padded string

## 0.1.0

* add `readerIndex` and `writerIndex` extension properties
* add operator overloads for `contains` functions
