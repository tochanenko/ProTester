import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {Library} from "../../models/library.model";
import {LibraryFilter} from "./library-filter.model";
import {LibraryManageService} from "../../services/library/library-manage.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-library-search',
  templateUrl: './library-search.component.html',
  styleUrls: ['./library-search.component.css']
})
export class LibrarySearchComponent implements OnInit {
  searchForm: FormGroup;
  dataSource: Library[];
  pageEvent: PageEvent;

  columnsToDisplay: string[] = ['NAME', 'DESCRIPTION', 'CONF'];
  pageSizeOptions: number[] = [5, 10, 25, 50];
  librariesCount = 10;
  libraryFilter: LibraryFilter = new LibraryFilter();

  private subscription: Subscription;

  constructor(private formBuilder: FormBuilder, private libraryService: LibraryManageService) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
    this.getLibrariesCount();
  }

  searchByFilter(): void {
    this.subscription = this.libraryService.getAllLibraries().subscribe(data => {
      this.dataSource = data;
    });
  }

  getLibrariesCount(): void {
    this.subscription = this.libraryService.getAllLibraries().subscribe( data => {
      this.librariesCount = data.length;
    });
  }

  onPaginateChange(event): void {
    console.log(event);
    this.libraryFilter.pageNumber = event.pageIndex;
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
