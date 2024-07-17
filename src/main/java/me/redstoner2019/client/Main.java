package me.redstoner2019.client;

import me.redstoner2019.audio.SoundProvider;
import me.redstoner2019.graphics.RenderI;
import me.redstoner2019.graphics.animation.Animation;
import me.redstoner2019.graphics.animation.AnimationFrame;
import me.redstoner2019.graphics.animation.AnimationProvider;
import me.redstoner2019.graphics.font.TextRenderer;
import me.redstoner2019.graphics.render.Renderer;
import me.redstoner2019.graphics.texture.TextureProvider;
import me.redstoner2019.gui.events.KeyPressedEvent;
import me.redstoner2019.gui.window.Window;
import me.redstoner2019.player.Player;
import org.json.JSONObject;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends Window {

    private Player player = new Player("Test",0,0,0,0);
    private HashMap<Integer,Boolean> keys = new HashMap<>();

    public Main(float x, float y, float width, float height) {
        super(x,y,width,height);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("localhost",8010);
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    while (!socket.isClosed()) {
                        JSONObject o = new JSONObject(new String(String.valueOf(ois.readObject())));
                        System.out.println(o.toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        t.start();

        init();
        setTitle("Amir Game");

        TextureProvider textureProvider = TextureProvider.getInstance();
        SoundProvider soundProvider = SoundProvider.getInstance();

        for(String s : listResources("textures")){

            textureProvider.loadTexture(s);
        }

        for(String s : listResources("audio")){
            soundProvider.loadSound(s);
        }

        AnimationFrame[] idleAnimationFrames = new AnimationFrame[5];
        for (int i = 0; i < 5; i++) {
            idleAnimationFrames[i] = new AnimationFrame(textureProvider.get("textures.idle." + i + ".png"), Color.WHITE);
        }
        Animation idleAnimation = new Animation(250,idleAnimationFrames);

        AnimationFrame[] runAnimationFrames = new AnimationFrame[7];
        for (int i = 0; i < 7; i++) {
            runAnimationFrames[i] = new AnimationFrame(textureProvider.get("textures.run." + i + ".png"), Color.WHITE);
        }
        Animation runAnimation = new Animation(75,runAnimationFrames);

        AnimationProvider animationProvider = AnimationProvider.getInstance();
        animationProvider.setAnimation("run",runAnimation);
        animationProvider.setAnimation("idle",idleAnimation);

        addRenderer(new RenderI() {
            @Override
            public void render(Renderer renderer, TextRenderer textRenderer, TextureProvider textureProvider, SoundProvider soundProvider) {
                player.update(getDeltaTime());

                if(keys.getOrDefault(GLFW.GLFW_KEY_A,false)){
                    player.setVx(-2f);
                }
                if(keys.getOrDefault(GLFW.GLFW_KEY_D,false)){
                    player.setVx(2f);
                }
                if(keys.getOrDefault(GLFW.GLFW_KEY_SPACE,false) && player.getVy() == 0){
                    player.setVy(3f);
                }

                player.render(renderer);
            }
        });

        addKeyPressedEvent(new KeyPressedEvent() {
            @Override
            public void keyPressedEvent(int i, int i1, int i2) {
                if(i1 == 1){
                    keys.put(i,true);
                }
                if(i1 == 0){
                    keys.put(i,false);
                }
            }
        });

        idleAnimation.play();
        idleAnimation.setRepeating(true);

        runAnimation.play();
        runAnimation.setRepeating(true);

        loop();
    }

    public static void main(String[] args) {
        new Main(0,0,1280,720);
    }

    private static Path getJarPath() throws URISyntaxException {
        URL jarUrl = me.redstoner2019.util.Resources.class.getProtectionDomain().getCodeSource().getLocation();
        return Paths.get(jarUrl.toURI());
    }
    public static java.util.List<String> listResources(String resourceFolder){
        try{
            List<String> files = new ArrayList<>();
            for (Path p : Files.list(Path.of("src/main/resources/" + resourceFolder)).toList()) {
                files.add(p.toString().substring("src/main/resources/".length()));
            }
            return files;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}