package account.json;

import account.utils.DateConverterUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.time.LocalDate;

// TODO: check LocalDate convertions
@JsonComponent
public class JsonLocalDateConverter {

    public static class Serialize extends JsonSerializer<LocalDate> {
        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//            try {
                var dateString = DateConverterUtil.fromLocalDate(value);
                if ( dateString == null) {
                    gen.writeNull();
                } else {
                    gen.writeString(dateString);
                }
//            } catch (Exception exception) { }
        }
//        public void se
    }

    public static class Deserialize extends JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            try {
                return DateConverterUtil.fromString(p.getText());
            } catch (Exception ex) {}
            return (LocalDate) new Object();
        }
    }
}
