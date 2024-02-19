import java.io.Serializable;
import java.util.ArrayList;
import java.text.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.Iterator;

public class DecisionTree implements Serializable {

    DTNode rootDTNode;
    int minSizeDatalist; //minimum number of datapoints that should be present in the dataset so as to initiate a split

    // Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
    public static final long serialVersionUID = 343L;

    public DecisionTree(ArrayList<Datum> datalist, int min) {
        minSizeDatalist = min;
        rootDTNode = (new DTNode()).fillDTNode(datalist);
    }

    class DTNode implements Serializable {
        //Mention the serialVersionUID explicitly in order to avoid getting errors while deserializing.
        public static final long serialVersionUID = 438L;
        boolean leaf;
        int label = -1;      // only defined if node is a leaf
        int attribute; // only defined if node is not a leaf
        double threshold;  // only defined if node is not a leaf

        DTNode left, right; //the left and right child of a particular node. (null if leaf)

        DTNode() {
            leaf = true;
            threshold = Double.MAX_VALUE;
        }


        /************************************************************************************************************************************************************************************************************************************************/
        // this method takes in a datalist (ArrayList of type datum). It returns the calling DTNode object
        // as the root of a decision tree trained using the datapoints present in the datalist variable and minSizeDatalist.
        // Also, KEEP IN MIND that the left and right child of the node correspond to "less than" and "greater than or equal to" threshold
        DTNode fillDTNode(ArrayList<Datum> datalist) {

            if (datalist.size() < minSizeDatalist) {

                this.leaf = true;
                this.label = findMajority(datalist);
                return this;
            } else {
                boolean sameLabel = true;
                for (Datum data : datalist) {
                    if (data.y != datalist.get(0).y) {
                        sameLabel = false;
                        break;
                    }
                }
                if (sameLabel) {
                    this.leaf = true;
                    this.label = findMajority(datalist);
                    return this;
                }






                /*********************************/
                int bestAttribute = -1;
                double bestThreshold = -1;
                double bestAvgEntropy = Double.MAX_VALUE;

                int sizeAttribute = datalist.get(0).x.length;
                for (int i = 0; i < sizeAttribute; i++) {
                    for (Datum data : datalist) {
                        double curThreshold = data.x[i];

                        ArrayList<Datum> leftList = new ArrayList<>();
                        ArrayList<Datum> rightList = new ArrayList<>();
                        for (Datum element : datalist) {
                            if (element.x[i] < curThreshold) leftList.add(element);
                            else rightList.add(element);
                        }
                        double curAvg = (((double) leftList.size() / (double) datalist.size()) * calcEntropy(leftList)) + (((double) rightList.size() / (double) datalist.size()) * calcEntropy(rightList));
                        if (curAvg < bestAvgEntropy) {
                            bestAvgEntropy = curAvg;
                            bestAttribute = i;
                            bestThreshold = curThreshold;
                        }
                    }
                }
                /********************************/


                //data1, data2

                this.label = findMajority(datalist);
                this.attribute = bestAttribute;
                this.threshold = bestThreshold;
                this.leaf = false;

                ArrayList<Datum> data1 = new ArrayList<>();
                ArrayList<Datum> data2 = new ArrayList<>();

                for (Datum data : datalist) {
                    if (data.x[this.attribute] < this.threshold) {
                        data1.add(data);
                    } else {
                        data2.add(data);
                    }
                }
//                System.out.println(this.attribute + "      " + this.threshold +"      " +data1.size()+"      " +data2.size() );
                this.left = (new DTNode()).fillDTNode(data1);
                this.right = (new DTNode()).fillDTNode(data2);
                return this;
            }
        }

        //private helper method


        // This is a helper method. Given a datalist, this method returns the label that has the most
        // occurrences. In case of a tie it returns the label with the smallest value (numerically) involved in the tie.
        int findMajority(ArrayList<Datum> datalist) {

            int[] votes = new int[2];

            //loop through the data and count the occurrences of datapoints of each label
            for (Datum data : datalist) {
                votes[data.y] += 1;
            }

            if (votes[0] >= votes[1])
                return 0;
            else
                return 1;
        }


        // This method takes in a datapoint (excluding the label) in the form of an array of type double (Datum.x) and
        // returns its corresponding label, as determined by the decision tree
        int classifyAtNode(double[] xQuery) {

            //ADD CODE HERE
            if(this==null) return 0;
            if (this.leaf) return this.label;
            if (xQuery[this.attribute] < this.threshold) return this.left.classifyAtNode(xQuery);
            else return this.right.classifyAtNode(xQuery);
        }


        //given another DTNode object, this method checks if the tree rooted at the calling DTNode is equal to the tree rooted
        //at DTNode object passed as the parameter
        public boolean equals(Object dt2) {

            //ADD CODE HERE

            if (!(dt2 instanceof DTNode)) return false;

            DTNode other = (DTNode) dt2;

            if (this.leaf || other.leaf) {
                if (this.leaf && other.leaf) {
                    return this.label == other.label;
                } else
                    return false;
            } else {
                if (this.attribute != other.attribute || Double.compare(this.threshold, other.threshold) != 0)
                    return false;
            }


            //recursively equals
            boolean leftRecur = ((this.left == null && other.left == null) || this.left != null && this.left.equals(other.left));
            boolean rightRecur = ((this.right == null && other.right == null) || this.right != null && this.right.equals(other.right));

//            return this.left.equals(other.left)&& this.right.equals(other.right);

            return (leftRecur && rightRecur);

        }


        //dummy code.  Update while completing the assignment.

    }


    //Given a dataset, this returns the entropy of the dataset
    double calcEntropy(ArrayList<Datum> datalist) {
        double entropy = 0;
        double px = 0;
        float[] counter = new float[2];
        if (datalist.size() == 0)
            return 0;
        double num0 = 0.00000001, num1 = 0.000000001;

        //calculates the number of points belonging to each of the labels
        for (Datum d : datalist) {
            counter[d.y] += 1;
        }
        //calculates the entropy using the formula specified in the document
        for (int i = 0; i < counter.length; i++) {
            if (counter[i] > 0) {
                px = counter[i] / datalist.size();
                entropy -= (px * Math.log(px) / Math.log(2));
            }
        }

        return entropy;
    }


    // given a datapoint (without the label) calls the DTNode.classifyAtNode() on the rootnode of the calling DecisionTree object
    int classify(double[] xQuery) {
        return this.rootDTNode.classifyAtNode(xQuery);
    }

    // Checks the performance of a DecisionTree on a dataset
    // This method is provided in case you would like to compare your
    // results with the reference values provided in the PDF in the Data
    // section of the PDF
    String checkPerformance(ArrayList<Datum> datalist) {
        DecimalFormat df = new DecimalFormat("0.000");
        float total = datalist.size();
        float count = 0;

        for (int s = 0; s < datalist.size(); s++) {
            double[] x = datalist.get(s).x;
            int result = datalist.get(s).y;
            if (classify(x) != result) {
                count = count + 1;
            }
        }

        return df.format((count / total));
    }


    //Given two DecisionTree objects, this method checks if both the trees are equal by
    //calling onto the DTNode.equals() method
    public static boolean equals(DecisionTree dt1, DecisionTree dt2) {
        boolean flag = true;
        flag = dt1.rootDTNode.equals(dt2.rootDTNode);
        return flag;
    }




    public static void main(String[] args) {

        DataReader dr = ReadFile.getCSVDataReader(  "data_partial_overlap.csv");
        dr.splitTrainTestData(0.5);
        DecisionTree dt = new DecisionTree (dr.trainData,  1);
        DecisionTree  dt2 = new DecisionTree (dr.trainData,  2);
        DecisionTree dt3 = new DecisionTree(dr.trainData,  4);
        DecisionTree  dt4 = new DecisionTree(dr.trainData,  8);
        DecisionTree dt5 = new DecisionTree (dr.trainData,  16);
        DecisionTree dt6 = new DecisionTree(dr.trainData, 32);
        DecisionTree dt7 = new DecisionTree (dr.trainData,  64);
        DecisionTree dt8 = new DecisionTree (dr.trainData,  128);

        String performance1 = dt.checkPerformance(dr.testData);
        String performance2 = dt2.checkPerformance (dr.testData);
        String performance3= dt3.checkPerformance (dr.testData);
        String performance4= dt4.checkPerformance (dr.testData);
        String performance5 = dt5.checkPerformance (dr.testData);
        String performance6 = dt6.checkPerformance (dr.testData);
        String performance7 = dt7.checkPerformance (dr.testData);
        String performance8 = dt8.checkPerformance (dr.testData);

        System.out.println(dt.minSizeDatalist+"    "+performance1);
        System.out.println(dt.minSizeDatalist+"    "+performance2);
        System.out.println(dt.minSizeDatalist+"    "+performance3);
        System.out.println(dt.minSizeDatalist+"    "+performance4);
        System.out.println(dt.minSizeDatalist+"    "+performance5);
        System.out.println(dt.minSizeDatalist+"    "+performance6);
        System.out.println(dt.minSizeDatalist+"    "+performance7);
        System.out.println(dt.minSizeDatalist+"    "+performance8);



    }


}
