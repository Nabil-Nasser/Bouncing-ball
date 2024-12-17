package MonsterRush;

import Texture.TextureReader;
import com.sun.opengl.util.GLUT;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

public class MonsterRushEventListener implements GLEventListener,KeyListener {
    float screenHeight = 200;
    float screenWidth = 200;
    float xMax = screenWidth / 2f;
    float xMin = -(screenWidth / 2f);
    float yMax = screenHeight / 2f;
    float yMin = -(screenHeight / 2f);
    /////////////////////////////////////////////////      for screen
    boolean gameRunning = false;
    public void setGameRunning(boolean running){
        this.gameRunning = running;
    }
    ///////////////////////////////////////////////// buttons
    int xSoldier = 0, ySoldier = (int)yMin+10;
    int animationIndex = 0, animationSpeed = 5,animationCounter = 0;
    //////////////////////////////////////////////////        solder
    int xMonster = 0 , yMonster = (int)yMax;
    int monsterIndex = 0;
    int movingMonsterSpeed = 1;
    int score;
    /////////////////////////////////////////////////         Monster
    double xBullet = 0, yBullet = 0;
    int movingBulletSpeed = 3;
    List<Bullet> bullets = new ArrayList<>();
    ///////////////////////////////////////////////////         Bullet
    int healthBarIndex = 0,maxHealth = 100,currHealth = maxHealth;
    float healthBarWidth = 100; // Set the width of the health bar
    float healthBarHeight = 10; // Set the height of the health bar
    float healthBarX = -60; // Position the health bar near the top-left
    float redHealthBarX = -60;
    float healthBarY = 85; // Position vertically near the top
    ////////////////////////////////////////////////////       health bar
    String assetsFolderName = "src//Assets//monsters";
    String[] textureNames = {"Man1.png","Man2.png","Man3.png","Man4.png","bullet2.png","HealthB.png","HealthA.png","Back.png"};
    String[] textureMonstersNames = new String[40];
    TextureReader.Texture[] texture = new TextureReader.Texture[textureNames.length];
    TextureReader.Texture[] textureMonster = new TextureReader.Texture[textureMonstersNames.length];
    int[] textures = new int[textureNames.length];
    int[] texturesMonsters = new int[textureMonstersNames.length];
    /////////////////////////////////////////////////////////////////////textures

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        fillMonsters();
        GL gl = glAutoDrawable.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glEnable(GL.GL_TEXTURE_2D);
        gl.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
        generateTextures(textureNames,texture,textures,gl);
        generateTextures(textureMonstersNames,textureMonster,texturesMonsters,gl);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        if (!gameRunning) {
            return;
        }
        GL gl = glAutoDrawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glLoadIdentity();
        {
            DrawBackground(gl);
            handleKeyPress();
            DrawSprite(gl, xSoldier, ySoldier, textures, animationIndex, 0, 2,2); // draw the soldier
        }//^Soldier & back-ground
        {
            makeBulletsToHitMonsters(gl);
        }//^Soldier Bullet
        {
            DrawSprite(gl,xMonster,yMonster,texturesMonsters,monsterIndex,0,3,3);
            yMonster -= movingMonsterSpeed; // here the monster going down
            if (yMonster < yMin + 10) {
                currHealth = Math.max(0, currHealth - 10); // Decrease health by 10, but not below 0
                if (currHealth == 0) {
                    System.out.println("GameOver");
                    JOptionPane.showMessageDialog((Component)null, "GameOver.", "GameOver", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
                // here we Reset monster position and choose a new random monster
                resetMonster();
                redHealthBarX -= 3;
            }

        }//^Monsters
        {
            float healthPercentage = currHealth / (float) maxHealth;
            DrawSprite(gl,(int)healthBarX,(int)healthBarY,textures,5,0,6,0.6f);// draw the white health bar
            DrawSprite(gl,(int)redHealthBarX,(int)healthBarY,textures,6,0,6*healthPercentage,0.6f);// draw the red health bar
        }//^Health Bar
        {
            renderText(gl, "Score: " + score, 0.95f, 0.85f); // Display score in the top-left corner
        }//^score
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glAutoDrawable, boolean b, boolean b1) {

    }
    public BitSet keyBits = new BitSet(256);

    public boolean isKeyPressed(int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
        if (keyCode == KeyEvent.VK_SPACE) {
//            xBullet = xSoldier + 2; // Initialize x position at soldier's position
//            yBullet = ySoldier + 8; // Set the star just above the soldier
            bullets.add(new Bullet(xSoldier + 2, ySoldier + 8));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyBits.clear(keyCode);
    }
    public void fillMonsters(){
        for (int i = 0; i < texturesMonsters.length; i++) {
            textureMonstersNames[i] = i+1 + ".png";
        }
    }

    public void makeBulletsToHitMonsters(GL gl) {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.y += movingBulletSpeed;
            DrawSprite(gl, bullet.x, bullet.y, textures, 4, 0, 0.15f,0.9f); // draw our Bullet Dim -> 1 : 6
            double dist = sqrdDistance((int) bullet.x, (int) bullet.y, xMonster, yMonster);
            double radii = Math.pow(0.5 * 0.1 * yMax + 0.5 * 0.1 * yMax, 2);
            if (dist <= radii) {
                resetMonster();
                iterator.remove();
                score++;
                continue;
            }

            if (bullet.y > yMax) {
                iterator.remove();
            }
        }
    }

    public void resetMonster() {
        xMonster = (int) (Math.random() * (screenWidth - 10) + 1) - (int) xMax + 5;
        yMonster = (int) yMax;
        monsterIndex = (int) (Math.random() * texturesMonsters.length);
    }

    public void renderText(GL gl, String text, float x, float y) {
        gl.glRasterPos2f(x/xMax + 0.7f,y/yMax +0.85f); // Set position for the text
        new GLUT().glutBitmapString(GLUT.BITMAP_HELVETICA_18,text);
    }
    public double sqrdDistance(int x, int y, int x1, int y1){
        return Math.pow(x-x1,2)+Math.pow(y-y1,2);
    }
    public void generateTextures(String[] textureNames, TextureReader.Texture[] texture, int[] textures, GL gl) {
        gl.glGenTextures(textureNames.length,textures,0);
        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i] , true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch( IOException e ) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }
    public void DrawSprite(GL gl,double x, double y,int[] textures, int index,float rotate, float scaleX, float scaleY){
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated( x/xMax, y/yMax, 0);
        gl.glScaled((0.05*scaleX), (0.05*scaleY), 1);
        gl.glRotated(rotate,0,0,1);
        drawFullScreenQuad(gl);
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }
    public void DrawBackground(GL gl){
        gl.glEnable(GL.GL_BLEND);	// Turn Blending On
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textures.length-1]);
        drawFullScreenQuad(gl);
        gl.glDisable(GL.GL_BLEND);  // Disable blending after drawing
    }
    public void drawFullScreenQuad(GL gl){
        gl.glBegin(GL.GL_QUADS);
        // Set each corner to align with the orthographic view boundaries
        // Map texture coordinates from 0 to 1 for the entire image
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom-left corner of the screen

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom-right corner

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top-right corner

        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top-left corner
        gl.glEnd();
    }
    public void handleKeyPress() {
        boolean flag = false;
        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (xSoldier > xMin+5) {
                xSoldier--;
            }
            flag = true;
        }
        if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (xSoldier < xMax-5) {
                xSoldier++;
            }
            flag = true;
        }
        if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (ySoldier > yMin+10) {
                ySoldier--;
            }
            flag = true;
        }
        if (isKeyPressed(KeyEvent.VK_UP)) {
            if (ySoldier < yMax-10) {
                ySoldier++;
            }
            flag = true;
        }
        if (flag) { // only increment if movement is detected
            animationCounter++;
            if (animationCounter >= animationSpeed) {
                animationIndex++;
                animationIndex %= 4;
                animationCounter = 0;
            }
        }
    }
    class Bullet{
        double x,y;
        public Bullet(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
