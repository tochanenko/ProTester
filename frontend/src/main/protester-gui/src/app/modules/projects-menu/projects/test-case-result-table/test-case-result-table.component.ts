import {Component, OnInit} from '@angular/core';
import {TestCaseResultService} from "../../../../services/test-case-result.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-test-case-result-table',
  templateUrl: './test-case-result-table.component.html',
  styleUrls: ['./test-case-result-table.component.css']
})
export class TestCaseResultTableComponent implements OnInit {

  displayedColumns = ['caseName', 'user', 'status', 'start', 'finish'];
  dataSource = [];
  projectId = -1;

  constructor(
    private testCaseResultService: TestCaseResultService,
    private router: Router,
    private route: ActivatedRoute,
    public datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        this.projectId = params['id'];
        this.testCaseResultService.getForProject(this.projectId).subscribe(
          results => {
            let testCaseResults = [];
            results.forEach(result => {
              console.log(result);
              testCaseResults.push({
                startDate: this.datePipe.transform(result['startDate'], 'short'),
                endDate: this.datePipe.transform(result['endDate'], 'short'),
                caseId: result['id'],
                caseName: result['testCase']['name'],
                userId: result['user']['id'],
                userName: result['user']['username'],
                status: result['status']
              })
            });
            this.dataSource = testCaseResults;
          }
        );
      })
  }
}

