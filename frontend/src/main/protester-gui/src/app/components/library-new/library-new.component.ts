import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-library-new',
  templateUrl: './library-new.component.html',
  styleUrls: ['./library-new.component.css']
})
export class LibraryNewComponent implements OnInit {
  searchForm: FormGroup;

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchForm = this.formBuilder.group({
      search: ['', []]
    })
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
