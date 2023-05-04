import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {ScenarioFilterModel} from "./scenario-filter.model";
import {Subscription} from "rxjs";
import {TestScenarioService} from "../../../../services/test-scenario/test-scenario-service";
import {OuterComponent} from "../../../../models/outer.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-scenario-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: OuterComponent[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  scenarioCount = 10;
  scenarioFilter: ScenarioFilterModel = new ScenarioFilterModel();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder,
              private scenarioService: TestScenarioService,
              private router: Router
  ) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
  }

  searchByFilter(): void {
    this.subscription = this.scenarioService.getAllWithFilter(this.scenarioFilter).subscribe(data =>
    {
      this.scenarioCount = data["totalItems"];
      data["list"].forEach(item => item.name = this.parseDescription(item.name));
      this.dataSource = data["list"];
    });
  }

  parseDescription(description: string | Object) {
    if (typeof description !== "object") {
      const regexp = new RegExp('(\\$\\{.+?\\})');
      let splitted = description.split(regexp);
      return splitted.map(sub_string => {
        if (sub_string.includes("${")) {
          return {
            text: sub_string.replace('${', '').replace('}', ''),
            input: true
          }
        } else {
          return {
            text: sub_string,
            input: false
          }
        }
      })
    } else {
      return description;
    }
  }

  goToView(id): void {
    if (id) {
      this.router.navigate([`projects-menu/scenarios/${id}`]).then();
    }
  }

  goToEdit(id): void {
    if (id) {
      this.router.navigate([`projects-menu/scenarios/${id}/edit`]).then();
    }
  }

  deleteScenario(id): void {
    this.subscription = this.scenarioService.delete(id).subscribe(data =>{
      if (data) {
        this.searchByFilter();
      }
    }, error => console.error(error.error.message));
  }

  onPaginateChange(event): void {
    this.scenarioFilter.pageNumber = event.pageIndex + 1;
    this.scenarioFilter.pageSize = event.pageSize;

    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onFormBlurs(): void {
    this.scenarioFilter.scenarioName = this.f.search.value;
    this.searchByFilter();
  }

  ngOnDestroy() : void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
