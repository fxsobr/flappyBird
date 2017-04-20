package br.com.githain.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private  SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
    private Texture gameOver;
    private Random numeroRandomico;
    private BitmapFont texto;
    private BitmapFont mensagem;
    private Circle colisaoPassaro;
    private Rectangle colisaoCanoTopo;
    private Rectangle colisaoCanoBaixo;

	private int larguraDispositivo;
	private int alturaDispositivo;
    private int iniciaJogo = 0;
    private int pontuacao;

	private float variacao = 0;
	private float velocidadeQueda;
	private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;
    private float espacoEntreCanos;
    private float deltaTime;
    private float alturaCanosRandomica;
    private boolean marcouPonto = false;

	@Override
	public void create () {

		batch = new SpriteBatch();
        numeroRandomico = new Random();
        colisaoPassaro = new Circle();
        texto = new BitmapFont();
        texto.setColor(Color.WHITE);
        texto.getData().setScale(6);
        mensagem =  new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");
        gameOver = new Texture("game_over.png");

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
		posicaoInicialVertical = alturaDispositivo /2;
        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 300;

	}

	@Override
	public void render () {

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;
        if (variacao > 2) variacao = 0;

        if (iniciaJogo == 0) {
            if (Gdx.input.justTouched()) {
                iniciaJogo = 1;
            }

        } else {

            velocidadeQueda++;
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical -= velocidadeQueda;

            if (iniciaJogo == 1){

                posicaoMovimentoCanoHorizontal -= deltaTime * 350;

                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -15;
                }

                if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    alturaCanosRandomica = numeroRandomico.nextInt(400) - 200;
                    marcouPonto = false;
                }


                if(posicaoMovimentoCanoHorizontal < 120){
                    if (!marcouPonto){
                        pontuacao++;
                        marcouPonto = true;
                    }
                }

            }else {

                if( Gdx.input.justTouched()){

                    iniciaJogo = 0;
                    pontuacao = 0;
                    velocidadeQueda = 0;
                    posicaoInicialVertical = alturaDispositivo / 2;
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                }
            }
        }
        batch.begin();
        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaCanosRandomica);
        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);
        texto.draw(batch, String.valueOf(pontuacao), larguraDispositivo /2, alturaDispositivo - 50);

        if(iniciaJogo == 2) {
            batch.draw(gameOver, larguraDispositivo / 2 - gameOver.getWidth() / 2 , alturaDispositivo / 2);
            mensagem.draw(batch, "Toque para Reiniciar!" ,larguraDispositivo / 2 - 200, alturaDispositivo / 2 - gameOver.getHeight() / 2);
        }

        batch.end();

        colisaoPassaro.set(120 + passaros[0].getWidth() /2 , posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() /2);
        colisaoCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal,alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaCanosRandomica,
                canoBaixo.getWidth(), canoBaixo.getHeight()
        );
        colisaoCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaCanosRandomica,
                canoTopo.getWidth(), canoTopo.getHeight()
        );


        //Teste de Colisão
        if (Intersector.overlaps( colisaoPassaro, colisaoCanoBaixo) || Intersector.overlaps(colisaoPassaro, colisaoCanoTopo)
                || posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo){
            iniciaJogo = 2;
        }

        }
}
