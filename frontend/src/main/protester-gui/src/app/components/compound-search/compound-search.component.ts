import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {LibraryFilter} from "../library-search/library-filter.model";
import {Subscription} from "rxjs";
import {CompoundManageService} from "../../services/compound-manage.service";
import {OuterComponent} from "../../models/outer.model";

@Component({
  selector: 'app-compound-search',
  templateUrl: './compound-search.component.html',
  styleUrls: ['./compound-search.component.css']
})
export class CompoundSearchComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: OuterComponent[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  librariesCount = 10;
  libraryFilter: LibraryFilter = new LibraryFilter();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder, private compoundService: CompoundManageService) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
    this.getLibrariesCount();
  }

  searchByFilter(): void {
    this.subscription = this.compoundService.getAllCompounds(this.libraryFilter).subscribe(data =>
    { console.log(this.libraryFilter);
      this.dataSource = data["list"];
    });
  }

  getLibrariesCount(): void {
    this.subscription = this.compoundService.getAllCompounds(this.libraryFilter).subscribe( data => {
      this.librariesCount = data["totalItems"];
    });
  }

  onPaginateChange(event): void {
    this.libraryFilter.pageNumber = event.pageIndex + 1;
    this.libraryFilter.pageSize = event.pageSize;

    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onFormBlurs(): void {
    this.libraryFilter.libraryName = this.f.search.value;
    this.searchByFilter();
  }

  ngOnDestroy() : void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

}
