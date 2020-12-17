import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Subscription} from "rxjs";
import {Router} from "@angular/router";
import {ProjectService} from "../../../../services/project.service";
import {StorageService} from "../../../../services/auth/storage.service";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit, OnDestroy {
  projectCreateForm: FormGroup;
  errorMessage = '';
  submitted = false;
  isSuccessful = false;
  isFailed = false;
  private subscription: Subscription;

  constructor(private router: Router,
              private formBuilder: FormBuilder,
              private projectService: ProjectService,
              private storageService: StorageService) {
  }

  ngOnInit(): void {
    this.createProjectCreateForm();
  }

  get f() {
    return this.projectCreateForm.controls;
  }

  createProjectCreateForm(): void {
    this.projectCreateForm = this.formBuilder.group({
      projectName: [null, Validators.compose([
        Validators.minLength(4),
        Validators.maxLength(50)])
      ],
      projectWebsiteLink: [null, Validators.compose([
        Validators.required
      ])]
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.projectCreateForm.invalid) {
      return;
    }

    const projectCreateResponse = {
      projectName: this.f.projectName.value,
      projectWebsiteLink: this.f.projectWebsiteLink.value,
      creatorId: this.storageService.getUser.id
    };

    this.subscription = this.projectService.create(projectCreateResponse)
      .subscribe(
        data => {
          this.isSuccessful = true;
          this.router.navigateByUrl('/projects-menu/projects').then();
        },
        err => {
          this.errorMessage = err.error.message;
          this.isFailed = true;
        }
      );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
