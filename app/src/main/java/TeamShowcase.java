import android.widget.TextView;

import androidx.annotation.NonNull;

public class TeamShowcase {
    private String teamName;
    private double averageScore;
    private int maxScore;
    private int minScore;

    private TextView textViewTeamName;
    private TextView textViewAverageScore;
    private TextView textViewMaxScore;
    private TextView textViewMinScore;

    public TeamShowcase(String teamName, int[] scores) {
        this.teamName = teamName;

        updateScores(scores);
    }

    public TeamShowcase(String teamName) {
        this.teamName = teamName;
    }

    public void updateScores(int[] scores) {
        int minIndex = 0;
        int maxIndex = 0;
        int sum = 0;

        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
            if (scores[i] < scores[minIndex]) {
                minIndex = i;
            }
            if (scores[i] > scores[maxIndex]) {
                maxIndex = i;
            }
        }

        this.averageScore = (double)(sum)/(double)(scores.length);
        this.maxScore = scores[maxIndex];
        this.minScore = scores[minIndex];

        if (textViewAverageScore != null) {
            textViewAverageScore.setText(Double.toString(averageScore).substring(0, Double.toString(averageScore).indexOf(".")+2));
        }
        if (textViewMaxScore != null) {
            textViewMaxScore.setText(Integer.toString(maxScore));
        }
        if (textViewMinScore != null) {
            textViewMinScore.setText(Integer.toString(minScore));
        }
    }

    public void setTextViewTeamName(TextView textViewTeamName) {
        this.textViewTeamName = textViewTeamName;
        this.textViewTeamName.setText(teamName);
    }

    public void setTextViewAverageScore(TextView textViewAverageScore) {
        this.textViewAverageScore = textViewAverageScore;
        this.textViewAverageScore.setText(Double.toString(averageScore).substring(0, Double.toString(averageScore).indexOf(".")+2));
    }

    public void setTextViewMaxScore(TextView textViewMaxScore) {
        this.textViewMaxScore = textViewMaxScore;
        this.textViewMaxScore.setText(Integer.toString(maxScore));
    }

    public void setTextViewMinScore(TextView textViewMinScore) {
        this.textViewMinScore = textViewMinScore;
        this.textViewMinScore.setText(Integer.toString(minScore));
    }

    public TextView getTextViewTeamName() {
        return textViewTeamName;
    }

    public TextView getTextViewAverageScore() {
        return textViewAverageScore;
    }

    public TextView getTextViewMaxScore() {
        return textViewMaxScore;
    }

    public TextView getTextViewMinScore() {
        return textViewMinScore;
    }

    public enum SortOrder {
        AVERAGE_SCORE,
        MAX_SCORE,
        MIN_SCORE,
        ALPHABETICALLY;
    }

    /**
     * @param otherTeam
     * @param order
     * @return {@code true} if {@code this} team is better at {@code order}, otherwise {@code false}
     */
    public boolean compare(TeamShowcase otherTeam, SortOrder order) {
        if (order == SortOrder.AVERAGE_SCORE) {
            return this.averageScore > otherTeam.averageScore;
        }
        else if (order == SortOrder.MAX_SCORE) {
            return this.maxScore > otherTeam.maxScore;
        }
        else if (order == SortOrder.MIN_SCORE) {
            return this.minScore > otherTeam.minScore;
        }
        else {
            return this.teamName.compareToIgnoreCase(otherTeam.teamName) > 0;
        }
    }

    public static TeamShowcase[] sortTeams(TeamShowcase[] teamShowcasesInput, SortOrder order) {
        TeamShowcase[] teamShowcases = new TeamShowcase[teamShowcasesInput.length];

        for (int i = 0; i < teamShowcases.length; i++) {
            teamShowcases[i] = teamShowcasesInput[i];
        }

        for (int i = 0; i < teamShowcases.length-1; i++) {
            for (int j = 0; j < teamShowcases.length - i - 1; j++) {
                if (!teamShowcases[j].compare(teamShowcases[j+1], order)) {
                    TeamShowcase temp = teamShowcases[j];
                    teamShowcases[j] = teamShowcases[j+1];
                    teamShowcases[j+1] = temp;
                }
            }
        }

        return teamShowcases;
    }
    
    public static TeamShowcase[] sortTeams(TeamShowcase[] teamShowcases) {
        return sortTeams(teamShowcases, SortOrder.AVERAGE_SCORE);
    }

    public static TeamShowcase getTeamWithName(TeamShowcase[] teamShowcases, String teamName) {
        for (int i = 0; i < teamShowcases.length; i++) {
            if (teamShowcases[i].teamName.equals(teamName)) {
                return teamShowcases[i];
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + teamName + ", average: " + averageScore + ", max: " + maxScore + ", min: " + minScore + "}, ";
    }
}
