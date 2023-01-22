## Figure Skating Skill Tracker
*Originally built in 4 phases as part of a term project for CPSC210* 

### Tracks daily practice and long term progress in skills

As a Figure Skater, I've always wanted a way to keep track of my practice sessions and skills  
in a way that was _organized_ and allowed me to _easily review_ what elements I needed to work on. 


#### This application will allow _Figure Skaters_ to:
- track skills practised on training sessions
- rate their progress on each of their skills


### User Stories

- As a user, I want to be able to add an element to a list of elements (training session)
- As a user, I want to be able to grade how well I performed an element
- As a user, I want to be able to track the start and end time of each training session
- As a user, I want to be able to add skills practiced to a training session


- As a user, I want to be able to save my training sessions to file
- As a user, I want to be able to be load my training sessions from file 

### Phase 4: Task 2

Mon Mar 28 12:45:27 PDT 2022 <br>
2022-02-01 training session from 10:00 am to 11:00 am created <br>
Mon Mar 28 12:45:30 PDT 2022 <br>
3A added to 2022-02-01 training session <br>
Mon Mar 28 12:45:39 PDT 2022 <br>
2A+3Lo added to 2022-02-01 training session <br>
Mon Mar 28 12:45:47 PDT 2022 <br>
2022-02-02 training session from 1:00 pm to 2:30 pm created <br>
Mon Mar 28 12:45:50 PDT 2022 <br>
3A added to 2022-02-02 training session <br>
Mon Mar 28 12:45:54 PDT 2022 <br>
FCSp4 added to 2022-02-02 training session <br>
Mon Mar 28 12:45:59 PDT 2022 <br>
StSq4 added to 2022-02-02 training session <br>

### Phase 4: Task 3

The associations between PracticeTrackerApp and TrainingSession as well as 
PracticeTrackerApp and Element are unnecessary and increase coupling. 

Since an Element is always a part of a TrainingSession, and a TrainingSession is always a part of a PracticeTracker, 
I would refactor this program by removing the unnecessary associations and creating getter methods to 
retrieve Element and TrainingSession information through PracticeTracker. 



