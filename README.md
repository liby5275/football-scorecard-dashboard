# football-scorecard-dashboard
A librabry for creating  the live dashboard of a football tournament.
It can be build using the maven command **maven clean install**
# Assumptions Made
1. Home team and Away team entities were taken as String for the simplicity. Ideally a Team can be a data class.
2. No need to consider any updateMatch scenarios like yello cards, substitution etc.
3. The in memory collection can be used at the same time, hence concurrency is needed
4. Update Score service only update the score. Not the playerScored or the minuteScored

