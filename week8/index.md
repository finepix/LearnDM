#马尔科夫链模型
##介绍
--------------------------------
马尔科夫模型是一个用于预测的统计模型，在人口，股票等问题上有很多应用。<br>
马尔科夫过程是一个离散随机过程，在这个过程中，过去的信息对于预测将来是无关的。即只与当前状态有关。（一阶模型，也有N阶马尔科夫模型，表示当前状态仅与之前的N个状态有关，跟再前面的无关。）<br>
时间和状态都是离散的马尔科夫过程，称为马尔科夫链，记为：![这里写图片描述](http://img.blog.csdn.net/20170415214943366?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjY0Nzk2NTU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)<br>
这样，我们根据上面介绍的，可以得出：![马尔科夫性质](http://img.blog.csdn.net/20170415215030429?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjY0Nzk2NTU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)<br>
<br>
对于有N个状态的一阶马尔科夫模型，每个状态可以转移到另一个状态（包括自己），则共有N^2次状态转移，可以用状态转移矩阵表示。
> Pij = P(Xn + 1 = i | Xn = j)，其中Pij表示系统在时刻t处于状态j，在下一时刻t+l处于状态i的概率。

<br>
例如：一段文字中名词、动词、形容词出现的情况可以用有3个状态的y一阶马尔科夫模型M表示。状态s1:名词，状态s2:动词，状态s3:形容词。已知状态转移矩阵A，则状态序列O=“名动形名”（假定第一个词为名词）的概率为：
![马尔科夫例子1](/images/jiqixuexi/markov_example1.PNG)<br>
马尔科夫过程定义了以下三个部分：状态，初始向量，状态转移矩阵。<br>

##隐形马尔科夫模型
--------------------------------
隐形马尔科夫模型(HMM)是一个用来描述含有隐含未知参数的马尔可夫过程的统计模型。其难点是从可观察的参数中确定该过程的隐含参数。然后利用这些参数来作进一步的分析。<br>

隐性马尔科夫模型由初始概率分布，状态转移概率分布以及观测概率分布确定。<br>

隐性马尔科夫模型作了两个**基本假设**：<br>
1 齐次马尔科夫假设，即假设因此的马尔科夫链在任意时刻t的状态只依赖于其前一时刻的状态，与其他时刻的状态以及观测无关。<br>
2 观测独立性假设，即假设任意时刻的观测只依赖于该时刻的马尔科夫链的状态，与其他观测以及状态无关。<br>

HMM是马尔科夫链中的一种，只是它的状态不能直接被观察到，但是可以通过观察向量间接的反映出来，即每一个观察向量由一个具有相应概率密度分布的状态序列产生，又由于每一个状态也是随机分布的，所以HMM是一个双重随机过程。
<br>
<br>
某地只有两种天气：雨天和晴天。某人只做3件事：散步，购物，打扫卫生。
做什么事，跟天气很有关系。<br>
你知道这个地区的总的天气趋势,并且平时知道某人会做的事情。就是说这个隐马尔可夫模型的参数是已知的.<br>
```
    states = Rainy,Sunny
     observations =walk, shop, clean
     start_probability =Rainy: 0.6, Sunny: 0.4
   
     transition_probability = {
          Rainy: {Rainy: 0.7, Sunny: 0.3},
          Sunny : {Rainy: 0.4, Sunny: 0.6},
      }<br>
     emission_probability = {
          Rainy : {walk: 0.1, shop: 0.4, clean: 0.5},
          Sunny : {walk: 0.6, shop: 0.3, clean: 0.1},
      }
```            
可以观察到的状态序列和隐藏的状态序列是概率相关的。隐藏的状态和可观察到的状态之间有一种概率上的关系，也就是说某种隐藏状态 H 被认为是某个可以观察的状态 O1 是有概率的，假设为 P(O1 | H)。如果可以观察的状态有3种，那么很显然 P(O1 | H)+P(O2 | H)+ P(O3 | H) = 1。<br>
HMM是语音识别，人体行为识别，文字识别等领域应用非常广泛。<br>

**HMM的3个基本问题**：<br>
1 概率计算问题：根据模型计算制定序列O出现的概率。<br>
2 学习问题：根据已知观测序列，估计模型的参数，使得在该模型观测序列概率最大。<br>
3 预测问题：根据给定观测序列，求最有可能的对应的状态序列。<br>
之前在交大做的情感计算就是上面第3个问题。<br>

<br>
已知模型参数，计算某一特定输出序列的概率.通常使用forward算法解决.<br>
已知模型参数，寻找最可能的能产生某一特定输出序列的隐含状态的序列.通常使用Viterbi算法解决.<br>
已知输出序列，寻找最可能的状态转移以及输出概率.通常使用Baum-Welch算法以及Reversed Viterbi算法解决.<br>

**预测算法**：<br>
1 近似算法：在每个t时刻，选择最有可能出现的状态i，从而得到一个状态序列，将它作为预测的结果。<br>
这样做的缺点就是不能保证序列整体是最有可能的状态序列。<br>

2 维特比算法：实际上是动态规划来解决预测模型。<br>
根据动态规划原理，最优路径具有这样的特性：若是最优路径在时刻t通过节点i(t),那么这一路径从节点i(t)到终点i(T)的部分路径，对于所有节点i(t)到终点i(T)的路径必须是最优的。<br>
从终点i(T)开始，由后向前逐步求得节点，得到最优路径。这就是维特比算法。<br>
  
#Viterbi算法
> 前面简单介绍了一下该算法，这里就不多赘述了。

**输入**

 - 观测空间$O=\{ o_1,o_2.....,o_n\}$
 - 状态序列$S=\{ S_1,S_2.....,S_k\}$
 - 初始状态$\Pi=\{ \pi_1,\pi_2.....,\pi_k\}$
 - 观察序列$Y=\{ y_1,y_2.....,y_T\}$
 - 状态转移矩阵A （K*K），$A_ij$存储状态从$s_i$到$s_j$的概率
 - 矩阵B，存储观察的概率从Si到Oj。

**输出**

 - 最可能的隐藏状态$X=\{ x_1,x_2,.......,x_N\}$
 

####**示例**

> 参考https://en.wikipedia.org/wiki/Viterbi_algorithm
> 由于wiki中已经用Python实现了该算法，下面用java试着去实现以下

 1.**输入数据**

```java
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
```

 2.**状态转换图**
 ![这里写图片描述](https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/An_example_of_HMM.png/300px-An_example_of_HMM.png)

 3.**伪代码**
 ![这里写图片描述](http://img.blog.csdn.net/20170416164701586?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMjY0Nzk2NTU=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

4.**算法实现**

```java
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
```


  



#####参考：

> [隐马尔可夫模型（HMM）攻略](http://blog.csdn.net/likelet/article/details/7056068)<br>
		<a href="https://github.com/MangoLiu">MangoLiu</a>
> [wiki](https://en.wikipedia.org/wiki/Viterbi_algorithm)
> [github Viterbi java](https://github.com/hankcs/Viterbi/blob/master/src/com/hankcs/algorithm/WeatherExample.java)