package texture;

import java.lang.RuntimeException;
import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.lang.String;

/// Texture container that gives shared access to a store of textures.
public class TextureManager {

    /// Map of string container to texture.
    private HashMap<String, Texture> mTextures;

    /// Initialise texture map.
    public TextureManager() {

        mTextures = new HashMap<String, Texture>();

    }

    /// Add a new texture to the store using the specified key.
    /// \param key identifying key for the stored texture.
    /// \param texture the texture to store.
    public void addTexture(String key, Texture texture) throws RuntimeException {

        // Throw exception if texture is invalid.
        if (texture == null) {

            throw new IllegalArgumentException("Textures added to manager cannot be null");

        }

        // Throw exception if key is already in use.
        if (mTextures.containsKey(key)) {

            throw new RuntimeException("Key " + key + " is already in use in texture manager");

        }
        mTextures.put(key, texture);

    }

    /// Get a texture using an identifying string.
    /// \param key the string identifier for the stored texture.
    public Texture getTexture(String key) throws RuntimeException {

        // Get texture, throw texture if no texture can be found for key.
        Texture texture = mTextures.get(key);
        if (texture == null) {

            throw new RuntimeException("Could not find key " + key + "");

        }
        return texture;

    }

}
