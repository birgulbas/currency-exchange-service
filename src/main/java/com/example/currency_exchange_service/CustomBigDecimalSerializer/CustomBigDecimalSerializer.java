package com.example.currency_exchange_service.CustomBigDecimalSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class CustomBigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private static final DecimalFormat decimalFormat;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator(','); // Virgül ayırıcı olarak ayarla

        decimalFormat = new DecimalFormat("0.000", symbols);//virgülden sonra 4 sıfır
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(decimalFormat.format(value)); //valuye biçimlendirir string olarak yazar
    }
}
