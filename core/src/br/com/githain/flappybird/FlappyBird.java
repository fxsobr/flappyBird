package br.com.githain.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private  SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoBaixo;
	private Texture canoTopo;
    private Random numeroRandomico;
    private BitmapFont texto;


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
        texto = new BitmapFont();
        texto.setColor(Color.WHITE);
        texto.getData().setScale(6);
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		fundo = new Texture("fundo.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");

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
            posicaoMovimentoCanoHorizontal -= deltaTime * 200;
            velocidadeQueda++;

            if (Gdx.input.justTouched()) {
                velocidadeQueda = -15;
            }


            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical -= velocidadeQueda;

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


        }
        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaCanosRandomica);
        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);
        texto.draw(batch, String.valueOf(pontuacao), larguraDispositivo /2, alturaDispositivo - 50);

        batch.end();
        }
}
