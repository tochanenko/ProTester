import {Component, OnInit} from '@angular/core';
import {TestCaseResultService} from "../../../../services/test-case-result.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatePipe} from "@angular/common";
import {PageEvent} from "@angular/material/paginator";

@Component({
  selector: 'app-test-case-result-table',
  templateUrl: './test-case-result-table.component.html',
  styleUrls: ['./test-case-result-table.component.css']
})
export class TestCaseResultTableComponent implements OnInit {

  displayedColumns = ['caseName', 'user', 'status', 'start', 'finish'];
  testCaseResults = [];
  dataSource = [];
  projectId = -1;
  processed = false;

  pageEvent: PageEvent;
  pageIndex = 0;
  pageSize = 10;
  length: number;

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
            this.testCaseResults = testCaseResults;
            this.length = testCaseResults.length;
            this.dataSource = this.testCaseResults.slice(this.pageIndex * this.pageSize, (this.pageIndex + 1) * this.pageSize);
            this.processed = true;
          }
        );
      })
  }

  updateList(event?: PageEvent): PageEvent {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.dataSource = this.testCaseResults.slice(event.pageIndex * event.pageSize, (event.pageIndex + 1) * event.pageSize);
    return event;
  }
}

