import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PageEvent} from "@angular/material/paginator";
import {Library} from "../../models/library.model";
import {LibraryFilter} from "./library-filter.model";
import {LibraryManageService} from "../../services/library/library-manage.service";

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

  constructor(private formBuilder: FormBuilder, private libraryService: LibraryManageService) { }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
    this.searchByFilter();
    this.getLibrariesCount();

  }

  searchByFilter(): void {
    this.libraryService.getAllLibraries().subscribe(data =>
    {
      let pageSize = this.libraryFilter.pageSize;
      let pageNumber = this.libraryFilter.pageNumber;
      let range = pageSize * pageNumber;
      this.dataSource = data['libraries'].slice(range, pageSize + range);
    });
  }

  getLibrariesCount(): void {
    this.libraryService.getAllLibraries().subscribe( data => {
      this.librariesCount = data['libraries'].length;
    });
  }

  onPaginateChange(event): void {
    this.libraryFilter.pageNumber = event.pageIndex;
    this.libraryFilter.pageSize = event.pageSize;

    this.searchByFilter();
  }


  get f() {
    return this.searchForm.controls;
  }

  onChanges(): void {
    console.log(this.f.search.value);
  }

  onSubmit(): void {
    let a = this.f.search.value
    console.log(a);
  }

}
