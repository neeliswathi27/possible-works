import java.util.*;
import java.util.regex.*;

public class SecretSharing {
    public static void main(String[] args) {
        // Simulating JSON input as a string
        String jsonData = "{ \"keys\": { \"n\": 4, \"k\": 3 }, " +
                "\"1\": { \"base\": \"10\", \"value\": \"4\" }, " +
                "\"2\": { \"base\": \"2\", \"value\": \"111\" }, " +
                "\"3\": { \"base\": \"10\", \"value\": \"12\" }, " +
                "\"6\": { \"base\": \"4\", \"value\": \"213\" } }";

        // Parsing JSON manually
        Map<Integer, Integer> points = parseJsonManually(jsonData);
        
        // Convert points into (x, y) pairs
        List<int[]> xyPoints = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : points.entrySet()) {
            xyPoints.add(new int[]{entry.getKey(), entry.getValue()});
        }

        // Solve for the constant term using Lagrange Interpolation
        int constantTerm = lagrangeInterpolation(xyPoints);
        System.out.println("Secret Constant (c): " + constantTerm);
    }

    // Manually parse JSON-like data and extract (x, y) pairs
    public static Map<Integer, Integer> parseJsonManually(String jsonData) {
        Map<Integer, Integer> points = new HashMap<>();
        Pattern pattern = Pattern.compile("\"(\\d+)\": \\{ \"base\": \"(\\d+)\", \"value\": \"([^\"]+)\" \\}");
        Matcher matcher = pattern.matcher(jsonData);

        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1)); // Extract x value
            int base = Integer.parseInt(matcher.group(2)); // Extract base
            int y = Integer.parseInt(matcher.group(3), base); // Convert y from given base
            points.put(x, y);
        }
        return points;
    }

    // Lagrange Interpolation to find the constant term
    public static int lagrangeInterpolation(List<int[]> points) {
        int constantTerm = 0;
        for (int i = 0; i < points.size(); i++) {
            int x_i = points.get(i)[0];
            int y_i = points.get(i)[1];
            double term = y_i;
            
            for (int j = 0; j < points.size(); j++) {
                if (i != j) {
                    int x_j = points.get(j)[0];
                    term *= (double) x_j / (x_j - x_i);
                }
            }
            constantTerm += term;
        }
        return (int) Math.round(constantTerm);
    }
}
