import {Component, OnInit} from '@angular/core';
import {TestCaseResultService} from "../../../services/test-case-result.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-test-case-result-table',
  templateUrl: './test-case-result-table.component.html',
  styleUrls: ['./test-case-result-table.component.css']
})
export class TestCaseResultTableComponent implements OnInit {

  displayedColumns = ['caseName', 'user', 'status', 'start', 'finish', 'actions'];
  dataSource = [];
  projectId = -1;

  constructor(
    private testCaseResultService: TestCaseResultService,
    private router: Router,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(
      params => {
        console.log(params);
        this.projectId = params['id'];
        this.testCaseResultService.getForProject(this.projectId).subscribe(
          results => {
            let testCaseResults = [];
            for (let result in results) {
              testCaseResults.push({
                caseId: result['id'],
                caseName: result['testCase']['name'],
                userId: result['user']['id'],
                userName: result['user']['username'],
                status: result['status'],
                startDate: result['startDate'],
                endDate: result['endDate']
              });
            }
            this.dataSource = testCaseResults;
          }
        );
      })
  }
}

