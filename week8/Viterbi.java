package hmm;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

/**
 * @author f-zx
 *
 */
public class Viterbi {

	private int[] obs = {};
	private int[] states = {};
	private double[] start_p = null;
	private double[][] trans_p = null;
	private double[][] emit_p = null;
	
	/**
	 * @param obs
	 * @param state
	 * @param start_p
	 * @param trans_p
	 * @param emit_p
	 */
	public Viterbi(int[] obs, int[] state, double[] start_p, double[][] trans_p, double[][] emit_p) {
		super();
		this.obs = obs;
		this.states = state;
		this.start_p = start_p;
		this.trans_p = trans_p;
		this.emit_p = emit_p;
	}
	
    /**
     * ���HMMģ��
     * @param obs �۲�����
     * @param states ��״̬
     * @param start_p ��ʼ���ʣ���״̬��
     * @param trans_p ת�Ƹ��ʣ���״̬��
     * @param emit_p ������� ����״̬����Ϊ��״̬�ĸ��ʣ�
     * @return ����ܵ�����
     */
    protected int[] getViterbiPath()
    {
        double[][] V = new double[obs.length][states.length];
        int[][] path = new int[states.length][obs.length];

        for (int y : states)
        {
            V[0][y] = start_p[y] * emit_p[y][obs[0]];
            path[y][0] = y;
        }

        for (int t = 1; t < obs.length; ++t)
        {
            int[][] newpath = new int[states.length][obs.length];

            for (int y : states)
            {
                double prob = -1;
                int state;
                for (int y0 : states)
                {
                    double nprob = V[t - 1][y0] * trans_p[y0][y] * emit_p[y][obs[t]];
                    if (nprob > prob)
                    {
                        prob = nprob;
                        state = y0;
                        // ��¼������
                        V[t][y] = prob;
                        // ��¼·��
                        System.arraycopy(path[state], 0, newpath[y], 0, t);
                        newpath[y][t] = y;
                    }
                }
            }

            path = newpath;
        }

        double prob = -1;
        int state = 0;
        for (int y : states)
        {
            if (V[obs.length - 1][y] > prob)
            {
                prob = V[obs.length - 1][y];
                state = y;
            }
        }

        return path[state];
    }


	/**
	 * �۲�״̬����
	 *
	 */
	enum Feel{
		Normal,
		Cold,
		Dizzy,
	}
	
	/**
	 * ����״̬
	 * */
	enum BodyState{
		Healthy,
		Fever;

	}

	/**
	 * ����java������ƻ�ȡö�������Ϣ��Ȼ����뵽map�й���ѯ
	 * @param path
	 */
	private void printPath(int [] path){
		HashMap<Integer, String> map  =  new LinkedHashMap<>();
		
		BodyState[] bss = BodyState.class.getEnumConstants();
		for (BodyState bodyState : bss) {
			map.put(bodyState.ordinal(), bodyState.name());
		}
		
		for(int i = 0; i < path.length ; i++){
			System.out.print(map.get(path[i]));
			if(i != path.length-1)
				System.out.print(" --> ");
		}
	}
	
	
	public static void main(String[] args) {

		//�۲�״̬
		int [] obs = new int[]{Feel.Normal.ordinal(),
				Feel.Cold.ordinal(),
				Feel.Dizzy.ordinal()};
		//���ص�״̬
		int [] states = new int[]{BodyState.Healthy.ordinal(),
				BodyState.Fever.ordinal()};
		//��ʼ����
		double[] start_p = new double[]{0.6,0.4};
		//״̬ת������
		double[][] trans_p = {{0.7,0.3},{0.4,0.6}};
		//ÿ��״̬��Ӧ�ĸ��ֹ۲����
		double[][] emit_p = {{0.5,0.4,0.1},{0.1,0.3,0.6}};
		
		Viterbi viterbi = new Viterbi(obs, states, start_p, trans_p, emit_p);
		int[]  path = viterbi.getViterbiPath();
		
		//��ӡ���ص�hmm
		viterbi.printPath(path);

		
	}
}
