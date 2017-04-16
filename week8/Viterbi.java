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
     * 求解HMM模型
     * @param obs 观测序列
     * @param states 隐状态
     * @param start_p 初始概率（隐状态）
     * @param trans_p 转移概率（隐状态）
     * @param emit_p 发射概率 （隐状态表现为显状态的概率）
     * @return 最可能的序列
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
                        // 记录最大概率
                        V[t][y] = prob;
                        // 记录路径
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
	 * 观测状态序列
	 *
	 */
	enum Feel{
		Normal,
		Cold,
		Dizzy,
	}
	
	/**
	 * 隐藏状态
	 * */
	enum BodyState{
		Healthy,
		Fever;

	}

	/**
	 * 利用java反射机制获取枚举类的信息，然后加入到map中供查询
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

		//观测状态
		int [] obs = new int[]{Feel.Normal.ordinal(),
				Feel.Cold.ordinal(),
				Feel.Dizzy.ordinal()};
		//隐藏的状态
		int [] states = new int[]{BodyState.Healthy.ordinal(),
				BodyState.Fever.ordinal()};
		//初始概率
		double[] start_p = new double[]{0.6,0.4};
		//状态转换矩阵
		double[][] trans_p = {{0.7,0.3},{0.4,0.6}};
		//每种状态对应的各种观测概率
		double[][] emit_p = {{0.5,0.4,0.1},{0.1,0.3,0.6}};
		
		Viterbi viterbi = new Viterbi(obs, states, start_p, trans_p, emit_p);
		int[]  path = viterbi.getViterbiPath();
		
		//打印隐藏的hmm
		viterbi.printPath(path);

		
	}
}
