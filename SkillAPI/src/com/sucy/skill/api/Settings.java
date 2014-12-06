package com.sucy.skill.api;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

/**
 * <p>Represents a set of settings that store configurable data for an object.</p>
 */
public class Settings
{
    private static final String BASE  = "-base";
    private static final String SCALE = "-scale";

    private final HashMap<String, Object> settings = new HashMap<String, Object>();

    /**
     * Sets the value for a setting. You should only provide a
     * String, int, boolean, or double as those are the only
     * supported types.
     *
     * @param key   setting key
     * @param value setting value
     */
    public void set(String key, Object value)
    {
        settings.put(key, value);
    }

    /**
     * <p>Defines a new scaling setting</p>
     * <p>Values are overwritten after the configuration
     * is loaded. Using this method is simply to define
     * the default values before configuration changes.</p>
     * <p>You should not use this method after the initial setup.</p>
     *
     * @param key   attribute name
     * @param base  base value
     * @param scale value scale
     */
    public void set(String key, double base, double scale)
    {
        settings.put(key + BASE, base);
        settings.put(key + SCALE, scale);
    }

    /**
     * <p>Sets the base value of a scaling setting</p>
     * <p>If the scaling setting is not set, this will default
     * the scale of the setting to 0.</p>
     * <p>This is used to override the default values.
     * You should not use this method. When defining scaling settings,
     * use set(String, double, double)</p>
     *
     * @param key   scaling setting name
     * @param value new base value
     */
    public void setBase(String key, double value)
    {
        if (!settings.containsKey(key + SCALE))
        {
            settings.put(key + SCALE, 0.0);
        }
        settings.put(key + BASE, value);
    }

    /**
     * <p>Sets the bonus value of a scaling setting</p>
     * <p>If the scaling setting is not set, this will default
     * the base of the setting to 0.</p>
     * <p>This is used by the API to override the default values.
     * You should not use this method. When defining scaling settings,
     * use set(String, double, double)</p>
     *
     * @param key   scaling setting name
     * @param value new scale value
     */
    public void setScale(String key, double value)
    {
        if (!settings.containsKey(key + BASE))
        {
            settings.put(key + BASE, 0.0);
        }
        settings.put(key + SCALE, value);
    }

    /**
     * Retrieves a double value from the settings. If the setting is
     * not set, this will instead return 0.
     *
     * @param key setting key
     *
     * @return double setting value
     */
    public double get(String key)
    {
        if (settings.containsKey(key))
        {
            return Double.parseDouble(settings.get(key).toString());
        }
        else
        {
            return 0;
        }
    }

    /**
     * Retrieves an integer value from the settings. If the setting is
     * not set, this will instead return 0.
     *
     * @param key setting key
     *
     * @return integer setting value
     */
    public int getInt(String key)
    {
        if (settings.containsKey(key))
        {
            return Integer.parseInt(settings.get(key).toString());
        }
        else
        {
            return 0;
        }
    }

    /**
     * Retrieves a boolean value from the settings. If the setting is
     * not set, this will instead return false.
     *
     * @param key setting key
     *
     * @return boolean setting value
     */
    public boolean getBool(String key)
    {
        return settings.containsKey(key) && Boolean.parseBoolean(settings.get(key).toString());
    }

    /**
     * Retrieves a string value from the settings. If the setting is
     * not set, this will instead return null.
     *
     * @param key setting key
     *
     * @return String setting value
     */
    public String getString(String key)
    {
        if (settings.containsKey(key))
        {
            return settings.get(key).toString();
        }
        else
        {
            return null;
        }
    }

    /**
     * <p>Calculates a value for a scaling setting at a given level.</p>
     * <p>If the scaling setting does not exist, this will instead return 0.</p>
     *
     * @param key   scaling setting name
     * @param level level of scaling
     *
     * @return scaled setting value
     */
    public double get(String key, int level)
    {
        return get(key, level, 0);
    }

    /**
     * <p>Calculates a value for a scaling setting at a given level.</p>
     * <p>If the scaling setting does not exist, this will instead return
     * the provided default value.</p>
     *
     * @param key          scaling setting name
     * @param level        level of scaling
     * @param defaultValue the default value in case not set
     *
     * @return scaled setting value
     */
    public double get(String key, int level, double defaultValue)
    {
        if (!has(key))
        {
            return defaultValue;
        }
        return getBase(key) + getScale(key) * (level - 1);
    }

    /**
     * <p>Gets the base value of an attribute</p>
     * <p>If the attribute is not set, this will return 0.</p>
     *
     * @param key attribute name
     *
     * @return base value
     */
    public double getBase(String key)
    {
        if (!settings.containsKey(key + BASE))
        {
            return 0;
        }
        else
        {
            return Double.parseDouble(settings.get(key + BASE).toString());
        }
    }

    /**
     * <p>Gets the scale value of an attribute</p>
     * <p>If the attribute is not set, this will return 0.</p>
     *
     * @param key attribute name
     *
     * @return change in value per level
     */
    public double getScale(String key)
    {
        if (!settings.containsKey(key + SCALE))
        {
            return 0;
        }
        else
        {
            return Double.parseDouble(settings.get(key + SCALE).toString());
        }
    }

    /**
     * <p>Checks whether or not the setting is defined.</p>
     * <p>A setting is defined when it is set at any point using
     * any of the setter methods or while loading from the configuration.</p>
     *
     * @param key name of the setting
     *
     * @return true if defined, false otherwise
     */
    public boolean has(String key)
    {
        return settings.containsKey(key) || settings.containsKey(key + BASE);
    }

    /**
     * <p>Removes a setting.</p>
     * <p>If the setting is not set, this will not do anything.</p>
     *
     * @param key name of the attribute
     */
    public void remove(String key)
    {
        settings.remove(key);
        settings.remove(key + BASE);
        settings.remove(key + SCALE);
    }

    /**
     * <p>Checks to make sure the settings have a required scaling value.</p>
     * <p>If the scaling setting is not set, the scaling setting will be created with
     * the provided values.</p>
     * <p>If the attribute already exists, this will do nothing.</p>
     *
     * @param key          key of the setting to check
     * @param defaultBase  default base value
     * @param defaultScale default scale value
     */
    public void checkDefault(String key, double defaultBase, double defaultScale)
    {
        if (!has(key))
        {
            set(key, defaultBase, defaultScale);
        }
    }

    /**
     * <p>Saves settings to a configuration section.</p>
     * <p>If the config section is null, this does not do anything.</p>
     *
     * @param config configuration section to save to
     */
    public void save(ConfigurationSection config)
    {
        if (config == null)
        {
            return;
        }
        for (String key : settings.keySet())
        {
            ConfigurationSection section = config.createSection(key);
            section.set(key, settings.get(key));
        }
    }

    /**
     * <p>Loads attributes from a configuration section</p>
     * <p>If the section is null or has no keys, this will not do
     * anything.</p>
     * <p>Keys that do not point to valid sections for the base/scale
     * values will not be loaded.</p>
     * <p>Sections without a base or without a scale value will load
     * what's present and default the missing one to 0.</p>
     *
     * @param config configuration section to load from
     */
    public void load(ConfigurationSection config)
    {
        if (config == null)
        {
            return;
        }

        for (String key : config.getKeys(false))
        {
            settings.put(key, config.getString(key));
        }
    }
}
