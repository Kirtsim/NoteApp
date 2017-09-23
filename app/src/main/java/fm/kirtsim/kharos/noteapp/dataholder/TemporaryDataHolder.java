package fm.kirtsim.kharos.noteapp.dataholder;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by kharos on 23/09/2017
 */

@SuppressWarnings("WeakerAccess")
public class TemporaryDataHolder {

    private final Map<String, Object> mappedData;

    public TemporaryDataHolder() {
        mappedData = Maps.newHashMap();
    }

    public Object putData(String key, Object data) {
        if (data != null)
            return mappedData.putIfAbsent(key, data);
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getData(String key) {
        Object data = mappedData.get(key);
        if (data != null)
            return (T) data;
        return null;

    }

    public <T extends Object> T getDataOrDefault(String key, T _default) {
        T data = getData(key);
        if (data == null)
            data = _default;
        return data;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> T getAndRemoveData(String key) {
        Object data = mappedData.remove(key);
        if (data != null)
            return (T) data;
        return null;
    }

    public boolean removeData(String key) {
        return mappedData.remove(key) != null;
    }
}
